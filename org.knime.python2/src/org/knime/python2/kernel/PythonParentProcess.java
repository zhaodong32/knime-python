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
 *   Dec 14, 2019 (benjamin): created
 */
package org.knime.python2.kernel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Benjamin Wilhelm, KNIME GmbH, Konstanz, Germany
 */
public class PythonParentProcess implements AutoCloseable {

    private final Process m_process;

    private final ServerSocket m_serverSocket;

    private final Socket m_socket;

    private final Map<Integer, PythonProcess> m_subProcesses;

    PythonParentProcess(final Process process, final ServerSocket serverSocket, final Socket socket) {
        m_process = process;
        m_serverSocket = serverSocket;
        m_socket = socket;
        m_subProcesses = new HashMap<>();
    }

    PythonProcess startKernel(final int socketPort) throws IOException {
        sendToSocket(m_socket, CREATE_PROCESS, socketPort);
        return new PythonProcess(socketPort, m_socket);
    }

    private void startSocketListening() {
        new Thread(() -> {
            final byte[] data = new byte[12];
            final ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.order(ByteOrder.BIG_ENDIAN);
            try (final InputStream inStream = m_socket.getInputStream()) {
                while (!m_socket.isClosed()) {
                    // Read the next 12 bytes
                    int read = 0;
                    while (read < 12) {
                        read += inStream.read(data, read, 12 - read);
                    }
                    int command = buffer.getInt();
                    int processId = buffer.getInt();
                    int val = buffer.getInt();
                    handleMessage(command, processId, val);
                    buffer.rewind();
                }
            } catch (final IOException ex) {
                throw new IllegalStateException("Foo");
            }
        }).run();
    }

    private void handleMessage(final int command, final int processId, final int val) {
        // TODO constants for commands
        final PythonProcess process = m_subProcesses.get(processId);
        switch (command) {
            case 0:
                process.setIsAlive(val == 1);
                break;
            case 1:
                process.setExitValue(val);
                break;
            default:
                throw new IllegalStateException("Unknown command: " + command);
        }
    }

    @Override
    public void close() throws Exception {
        // TODO How to close the process properly?
        m_process.destroy();
        m_serverSocket.close();
        m_socket.close();
    }
}
