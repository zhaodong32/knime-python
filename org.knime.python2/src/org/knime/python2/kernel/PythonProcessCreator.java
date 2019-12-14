/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Dec 12, 2019 (benjamin): created
 */
package org.knime.python2.kernel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.knime.python.typeextension.PythonModuleExtensions;
import org.knime.python2.Activator;
import org.knime.python2.extensions.serializationlibrary.SerializationLibraryExtensions;

import com.google.common.base.Strings;

/**
 * @author Benjamin Wilhelm, KNIME GmbH, Konstanz, Germany
 */
public class PythonProcessCreator {

    private static final String PROCESS_CREATOR_RELATIVE_PATH = "py/ProcessCreator.py";

    private static final String PROCESS_CREATOR_PATH =
        Activator.getFile(Activator.PLUGIN_ID, PROCESS_CREATOR_RELATIVE_PATH).getAbsolutePath();

    private static final int CREATE_PROCESS = 0;

    private static final int IS_PROCESS_ALIVE = 1;

    private static final int DESTROY_PROCESS = 2;

    private static PythonProcessCreator instance;

    /**
     * @return the instance of the Python process creator
     */
    public static PythonProcessCreator instance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private static synchronized void createInstance() {
        if (instance == null) {
            instance = new PythonProcessCreator();
        }
    }

    private final Map<PythonKernelOptions, ParentProcess> m_processes;

    private PythonProcessCreator() {
        m_processes = new HashMap<>();
    }

    public PythonProcess createPythonProcess(final PythonKernelOptions options, final int socketPort) {
        final ParentProcess parentProcess = getParentProcess(options);
        try {
            System.out.println("Port: " + parentProcess.m_socket.getPort());
            System.out.println("Local port: " + parentProcess.m_socket.getLocalPort());
            sendToSocket(parentProcess.m_socket, CREATE_PROCESS, socketPort);
            return new PythonProcess(socketPort, parentProcess.m_socket);
        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private ParentProcess getParentProcess(final PythonKernelOptions options) {
        if (!m_processes.containsKey(options)) {
            final ParentProcess parent = setupParentProcess(options);
            m_processes.put(options, parent);
        }
        return m_processes.get(options);
    }

    private static ParentProcess setupParentProcess(final PythonKernelOptions options) {
        try {
            // Open the socket for communication
            final ServerSocket serverSocket = new ServerSocket(0);
            serverSocket.setSoTimeout(PythonKernel.getConnectionTimeoutInMillis());
            final Future<Socket> setupSocket = setupSocket(serverSocket);

            final String port = Integer.toString(serverSocket.getLocalPort());
            final String serializationLibraryPath = SerializationLibraryExtensions
                .getSerializationLibraryPath(options.getSerializationOptions().getSerializerId());
            // Build and start Python kernel that listens to the given port:
            final ProcessBuilder pb;
            if (options.getUsePython3()) {
                pb = options.getPython3Command().createProcessBuilder();
            } else {
                pb = options.getPython2Command().createProcessBuilder();
            }
            pb.inheritIO();
            // Use the -u options to force Python to not buffer stdout and stderror.
            Collections.addAll(pb.command(), "-u", PROCESS_CREATOR_PATH, port, serializationLibraryPath);
            // Add all python modules to PYTHONPATH variable.
            String existingPath = pb.environment().get("PYTHONPATH");
            existingPath = existingPath == null ? "" : existingPath;
            String externalPythonPath = PythonModuleExtensions.getPythonPath();

            externalPythonPath += File.pathSeparator + Activator.getFile(Activator.PLUGIN_ID, "py").getAbsolutePath();
            if (!Strings.isNullOrEmpty(options.getExternalCustomPath())) {
                externalPythonPath += File.pathSeparator + options.getExternalCustomPath();
            }
            if (!externalPythonPath.isEmpty()) {
                if (existingPath.isEmpty()) {
                    existingPath = externalPythonPath;
                } else {
                    existingPath = existingPath + File.pathSeparator + externalPythonPath;
                }
            }
            existingPath = existingPath + File.pathSeparator;
            pb.environment().put("PYTHONPATH", existingPath);

            // Start Python.
            final Process process = pb.start();

            // Wait for the socekt to be connected
            final Socket socket = setupSocket.get();
            return new ParentProcess(process, serverSocket, socket);
        } catch (final Exception ex) {
            // TODO: handle exception
            throw new IllegalStateException(ex);
        }
    }

    /** TODO copied from PythonKernel (combine?) */
    private static Future<Socket> setupSocket(final ServerSocket serverSocket) {
        return Executors.newSingleThreadExecutor().submit(serverSocket::accept);
    }

    /** Send the given integers on the given socket */
    private static void sendToSocket(final Socket socket, final int... values) throws IOException {
        final OutputStream outputStream = socket.getOutputStream();
        final ByteBuffer data = ByteBuffer.allocate(values.length * 4).order(ByteOrder.BIG_ENDIAN);
        for (final int v : values) {
            data.putInt(v);
        }
        outputStream.write(data.array());
        outputStream.flush();
    }

    /** Receive the next integer on the given socket */
    private static int reciveOnSocket(final Socket socket) throws IOException {
        final byte[] data = new byte[4];
        socket.getInputStream().read(data);
        return ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    /** A Python process that starts other python processes by forking itself */
    private static class ParentProcess implements AutoCloseable {

        private final Process m_process;

        private final ServerSocket m_serverSocket;

        private final Socket m_socket;

        private ParentProcess(final Process process, final ServerSocket serverSocket, final Socket socket) {
            m_process = process;
            m_serverSocket = serverSocket;
            m_socket = socket;
        }

        @Override
        public void close() throws Exception {
            // TODO How to close the process properly?
            m_process.destroy();
            m_serverSocket.close();
            m_socket.close();
        }
    }

    public static class PythonProcess {

        private final InputStream blockingStream = new InputStream() {

            @Override
            public int read() throws IOException {
                return -1;
            }
        };

        private final Socket m_parentSocket;

        private final int m_socketPort;

        public PythonProcess(final int socketPort, final Socket parentSocket) {
            m_socketPort = socketPort;
            m_parentSocket = parentSocket;
        }

        public InputStream getInputStream() {
            return blockingStream;
        }

        public InputStream getErrorStream() {
            return blockingStream;
        }

        public boolean isAlive() {
            try {
                sendToSocket(m_parentSocket, IS_PROCESS_ALIVE, m_socketPort);
                final int processPort = reciveOnSocket(m_parentSocket);
                //                if (processPort != m_socketPort) {
                //                    throw new IllegalStateException("Got message for another process");
                //                }
                final int is_alive = reciveOnSocket(m_parentSocket);
                return is_alive == 1;
            } catch (final Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        public void destroy() {
            try {
                sendToSocket(m_parentSocket, DESTROY_PROCESS, m_socketPort);
            } catch (final IOException ex) {
                throw new IllegalStateException(ex);
            }
        }

        public int exitValue() {
            // TODO implement
            return 0;
        }
    }
}
