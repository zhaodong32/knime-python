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
import java.io.InputStream;
import java.net.Socket;

/**
 * @author Benjamin Wilhelm, KNIME GmbH, Konstanz, Germany
 */
public class PythonProcess {

    private final InputStream blockingStream = new InputStream() {

        @Override
        public int read() throws IOException {
            return -1;
        }
    };

    private final Socket m_parentSocket;

    private final int m_socketPort;

    private boolean m_isAlive;

    private int m_exitValue;

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
        return m_exitValue;
    }

    void setExitValue(final int exitValue) {
        m_exitValue = exitValue;
    }

    void setIsAlive(final boolean isAlive) {
        m_isAlive = isAlive;
    }
}
