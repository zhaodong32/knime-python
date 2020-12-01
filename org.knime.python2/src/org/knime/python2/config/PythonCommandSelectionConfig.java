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
 *   Dec 1, 2020 (marcel): created
 */
package org.knime.python2.config;

import java.util.Locale;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.python2.PythonVersion;
import org.knime.python2.prefs.PythonPreferences;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 */
public final class PythonCommandSelectionConfig {

    private static final String CFG_PYTHON_VERSION = "pythonVersionOption";

    private static final String CFG_PYTHON2_COMMAND = "python2Command";

    private static final String CFG_PYTHON3_COMMAND = "python3Command";

    private PythonVersion m_pythonVersion = PythonPreferences.getPythonVersionPreference();

    private PythonCommandFlowVariableConfig m_python2CommandConfig = new PythonCommandFlowVariableConfig(
        CFG_PYTHON2_COMMAND, PythonVersion.PYTHON2, PythonPreferences::getCondaInstallationPath);

    private PythonCommandFlowVariableConfig m_python3CommandConfig = new PythonCommandFlowVariableConfig(
        CFG_PYTHON3_COMMAND, PythonVersion.PYTHON3, PythonPreferences::getCondaInstallationPath);

    /**
     * @return The configured Python version.
     */
    public PythonVersion getPythonVersion() {
        return m_pythonVersion;
    }

    /**
     * @param pythonVersion The configured Python version.
     */
    public void setPythonVersion(final PythonVersion pythonVersion) {
        m_pythonVersion = pythonVersion;
    }

    /**
     * @return The config of the Python 2 command.
     */
    public PythonCommandFlowVariableConfig getPython2CommandConfig() {
        return m_python2CommandConfig;
    }

    /**
     * @return The config of the Python 3 command.
     */
    public PythonCommandFlowVariableConfig getPython3CommandConfig() {
        return m_python3CommandConfig;
    }

    /**
     * Load this configuration from the given settings.
     *
     * @param settings The setting.
     * @throws InvalidSettingsException If loading the configuration failed.
     */
    public void loadFromSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        final String pythonVersionString = settings.getString(CFG_PYTHON_VERSION, getPythonVersion().getId());
        // Backward compatibility: older saved versions are all upper case.
        setPythonVersion(PythonVersion.fromId(pythonVersionString.toLowerCase(Locale.ROOT)));
        m_python2CommandConfig.loadSettingsFrom(settings);
        m_python3CommandConfig.loadSettingsFrom(settings);
    }

    /**
     * Save this configuration to the given settings.
     *
     * @param settings The setting.
     */
    public void saveToSettings(final NodeSettingsWO settings) {
        settings.addString(CFG_PYTHON_VERSION, getPythonVersion().getId());
        m_python2CommandConfig.saveSettingsTo(settings);
        m_python3CommandConfig.saveSettingsTo(settings);
    }
}
