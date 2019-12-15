
# -*- coding: utf-8 -*-
# ------------------------------------------------------------------------
#  Copyright by KNIME AG, Zurich, Switzerland
#  Website: http://www.knime.com; Email: contact@knime.com
#
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License, Version 3, as
#  published by the Free Software Foundation.
#
#  This program is distributed in the hope that it will be useful, but
#  WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, see <http://www.gnu.org/licenses>.
#
#  Additional permission under GNU GPL version 3 section 7:
#
#  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
#  Hence, KNIME and ECLIPSE are both independent programs and are not
#  derived from each other. Should, however, the interpretation of the
#  GNU GPL Version 3 ("License") under any applicable laws result in
#  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
#  you the additional permission to use and propagate KNIME together with
#  ECLIPSE with only the license terms in place for ECLIPSE applying to
#  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
#  license terms of ECLIPSE themselves allow for the respective use and
#  propagation of ECLIPSE together with KNIME.
#
#  Additional permission relating to nodes for KNIME that extend the Node
#  Extension (and in particular that are based on subclasses of NodeModel,
#  NodeDialog, and NodeView) and that only interoperate with KNIME through
#  standard APIs ("Nodes"):
#  Nodes are deemed to be separate and independent programs and to not be
#  covered works.  Notwithstanding anything to the contrary in the
#  License, the License does not apply to Nodes, you are not required to
#  license Nodes under the License, and you are granted a license to
#  prepare and propagate Nodes, in each case even if such Nodes are
#  propagated with or for interoperation with KNIME.  The owner of a Node
#  may freely choose the license terms applicable to such Node, including
#  when such Node is propagated with or for interoperation with KNIME.
# ------------------------------------------------------------------------

"""
@author Benjamin Wilhelm, KNIME GmbH, Konstanz, Germany
@author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
@author Christian Dietz, KNIME GmbH, Konstanz, Germany
"""
from __future__ import print_function
import sys
import socket
import struct
import os
from multiprocessing import Process
from PythonKernelLauncher import launch_python_kernel

RECEIVE_CREATE_PROCESS = 0
RECEIVE_IS_PROCESS_ALIVE = 1
RECEIVE_DESTROY_PROCESS = 2
SEND_IS_ALIVE = 0

def main(socket_port, serialization_lib):

    # Uncomment to enable debugging. You may want to disable breakpoints since they require an external debugger. See
    # debug_util module for information on how to set up a debug environment.
    sys.path.append('/home/benjamin/apps/eclipse/plugins/org.python.pydev.core_7.3.0.201908161924/pysrc')
    import debug_util
    debug_util.init_debug(enable_breakpoints=True, enable_debug_log=True, debug_log_to_stderr=False)

    processes = {}
    print('Parent process started at port ' + str(socket_port))

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as connection:
        connection.connect(('127.0.0.1', socket_port))

        # TODO send heartbeat

        while True:
            buffer = []
            while len(buffer) < 8:
                data = connection.recv(8 - len(buffer))
                buffer.extend(data)
            command = struct.unpack('>i', data[0:4])[0]
            process_port = struct.unpack('>i', data[4:8])[0]
            print('Recived message. Command ' + str(command) + ', Process port: ' + str(process_port))
            if command == RECEIVE_CREATE_PROCESS:
                processes[process_port] = launch_new_process(process_port, serialization_lib)
                print('Process created')
            elif command == RECEIVE_IS_PROCESS_ALIVE:
                is_alive = processes[process_port].is_alive()
                print('Process alive: ' + str(is_alive))
                data = struct.pack('>i', SEND_IS_ALIVE, process_port, int(is_alive))
                print('Sending reply ' + data)
                connection.sendall(data)
            elif command == RECEIVE_DESTROY_PROCESS:
                processes[process_port].terminate()
                print('Process terminated')

    return


def launch_new_process(process_port, serialization_lib):
    process = Process(target=launch_python_kernel, args=(process_port, serialization_lib))
    process.start()
    return process


if __name__ == '__main__':
    main(int(sys.argv[1]), sys.argv[2])