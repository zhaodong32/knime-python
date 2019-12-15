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
import java.net.ServerSocket;
import java.net.Socket;
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

    private final Map<PythonKernelOptions, PythonParentProcess> m_processes;

    private PythonProcessCreator() {
        m_processes = new HashMap<>();
    }

    public PythonProcess createPythonProcess(final PythonKernelOptions options, final int socketPort) {
        final PythonParentProcess parentProcess = getParentProcess(options);
        try {
            return parentProcess.startKernel(socketPort);
        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private PythonParentProcess getParentProcess(final PythonKernelOptions options) {
        if (!m_processes.containsKey(options)) {
            final PythonParentProcess parent = setupParentProcess(options);
            m_processes.put(options, parent);
        }
        return m_processes.get(options);
    }

    // TODO move to PythonParentProcess?
    private static PythonParentProcess setupParentProcess(final PythonKernelOptions options) {
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
            return new PythonParentProcess(process, serverSocket, socket);
        } catch (final Exception ex) {
            // TODO: handle exception
            throw new IllegalStateException(ex);
        }
    }

    /** TODO copied from PythonKernel (combine?) */
    private static Future<Socket> setupSocket(final ServerSocket serverSocket) {
        return Executors.newSingleThreadExecutor().submit(serverSocket::accept);
    }

}
