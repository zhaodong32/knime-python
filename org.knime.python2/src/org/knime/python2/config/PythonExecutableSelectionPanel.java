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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.knime.python2.PythonCommand;
import org.knime.python2.PythonVersion;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings("serial") // Not intended for serialization.
public final class PythonExecutableSelectionPanel extends JPanel {

    public static final String DEFAULT_TAB_NAME = "Executable Selection";

    private final Supplier<PythonCommand> m_defaultPython2Command;

    private final Supplier<PythonCommand> m_defaultPython3Command;

    /** {@code null} if no particular version is enforced. */
    private final PythonVersion m_enforcedVersion;

    private final JRadioButton m_python2Button;

    private final JRadioButton m_python3Button;

    private PythonCommand m_python2Command;

    private PythonCommand m_python3Command;

    private final List<BiConsumer<PythonVersion, PythonCommand>> m_listeners = new CopyOnWriteArrayList<>();

    /**
     * @param defaultPython2Command A supplier that returns the Python 2 command to use if no node-specific command is
     *            configured.
     * @param defaultPython3Command A supplier that returns the Python 3 command to use if no node-specific command his
     *            configured.
     */
    public PythonExecutableSelectionPanel(final Supplier<PythonCommand> defaultPython2Command,
        final Supplier<PythonCommand> defaultPython3Command) {
        this(defaultPython2Command, defaultPython3Command, null);
    }

    /**
     * Allows to enforce a certain Python version and hide this panel's Python version selection.
     *
     * @param defaultPython2Command A supplier that returns the Python 2 command to use if no node-specific command is
     *            configured.
     * @param defaultPython3Command A supplier that returns the Python 3 command to use if no node-specific command his
     *            configured.
     * @param enforcedPythonVersion Enforce the given Python version. Can be {@code null}, in which case the user is
     *            given the option to choose the Python version to use.
     */
    public PythonExecutableSelectionPanel(final Supplier<PythonCommand> defaultPython2Command,
        final Supplier<PythonCommand> defaultPython3Command, final PythonVersion enforcedPythonVersion) {
        m_defaultPython2Command = defaultPython2Command;
        m_defaultPython3Command = defaultPython3Command;
        m_enforcedVersion = enforcedPythonVersion;

        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;

        final JPanel versionPanel = new JPanel(new FlowLayout());
        versionPanel.setBorder(BorderFactory.createTitledBorder("Use Python version"));
        final ButtonGroup pythonVersionButtonGroup = new ButtonGroup();
        m_python2Button = new JRadioButton("Python 2");
        pythonVersionButtonGroup.add(m_python2Button);
        versionPanel.add(m_python2Button);
        m_python3Button = new JRadioButton("Python 3");
        pythonVersionButtonGroup.add(m_python3Button);
        versionPanel.add(m_python3Button);
        if (m_enforcedVersion != null) {
            versionPanel.setVisible(false);
        }
        add(versionPanel, gbc);

        final JPanel flowVarPanel = new JPanel();
        flowVarPanel.setLayout(new BoxLayout(flowVarPanel, BoxLayout.Y_AXIS));
        flowVarPanel.setBorder(BorderFactory.createTitledBorder("Conda environment propagation"));
        final ButtonGroup flowVarButtonGroup = new ButtonGroup();
        final JRadioButton useFlowVarButton = new JRadioButton(
            "Use \"conda.environment\" variable when available (otherwise default to KNIME Preferences)");
        flowVarButtonGroup.add(useFlowVarButton);
        flowVarPanel.add(useFlowVarButton);
        final JRadioButton useDefaultButton = new JRadioButton("Ignore \"conda.environment\" variable");
        flowVarButtonGroup.add(useDefaultButton);
        flowVarPanel.add(useDefaultButton);
        gbc.gridy++;
        add(flowVarPanel, gbc);

        gbc.gridy++;
        gbc.weighty = 1;
        add(new JLabel(""), gbc);

        m_python2Button
            .addActionListener(e -> notifyListenersIfCurrentPythonVersion(PythonVersion.PYTHON2, getPython2Command()));
        m_python3Button
            .addActionListener(e -> notifyListenersIfCurrentPythonVersion(PythonVersion.PYTHON3, getPython3Command()));
    }

    /**
     * @return The configured Python version.
     */
    public PythonVersion getPythonVersion() {
        final PythonVersion pythonVersion;
        if (m_enforcedVersion != null) {
            pythonVersion = m_enforcedVersion;
        } else if (m_python2Button.isSelected()) {
            pythonVersion = PythonVersion.PYTHON2;
        } else {
            pythonVersion = PythonVersion.PYTHON3;
        }
        return pythonVersion;
    }

    /**
     * @return The configured Python 2 command.
     */
    public PythonCommand getPython2Command() {
        return m_python2Command != null ? m_python2Command : m_defaultPython2Command.get();
    }

    /**
     * @return The configured Python 3 command.
     */
    public PythonCommand getPython3Command() {
        return m_python3Command != null ? m_python3Command : m_defaultPython3Command.get();
    }

    /**
     * The listener will be notified if the Python version and/or command selected via this panel has changed.
     *
     * @param listener The listener.
     */
    public void addChangeListener(final BiConsumer<PythonVersion, PythonCommand> listener) {
        m_listeners.add(listener);
    }

    /**
     * Removes the given listener.
     *
     * @param listener The listener.
     */
    public void removeChangeListener(final BiConsumer<PythonVersion, PythonCommand> listener) {
        m_listeners.remove(listener);
    }

    private void notifyListenersIfCurrentPythonVersion(final PythonVersion pythonVersion,
        final PythonCommand pythonCommand) {
        if (pythonVersion == getPythonVersion()) {
            for (final BiConsumer<PythonVersion, PythonCommand> listener : m_listeners) {
                listener.accept(pythonVersion, pythonCommand);
            }
        }
    }

    /**
     * Updates this panel with a new Python 2 command.
     *
     * @param python2Command The command.
     */
    public void updatePython2Command(final PythonCommand python2Command) {
        m_python2Command = python2Command;
        notifyListenersIfCurrentPythonVersion(PythonVersion.PYTHON2, python2Command);
    }

    /**
     * Updates this panel with a new Python 3 command.
     *
     * @param python3Command The command.
     */
    public void updatePython3Command(final PythonCommand python3Command) {
        m_python3Command = python3Command;
        notifyListenersIfCurrentPythonVersion(PythonVersion.PYTHON3, python3Command);
    }

    /**
     * Load this panel's settings from the given configuration.
     *
     * @param config The configuration.
     */
    public void loadSettingsFrom(final PythonSourceCodeConfig config) {
        m_python2Command = config.getPython2Command();
        m_python3Command = config.getPython3Command();
        // The Python version must be loaded after the commands since updating the buttons' selection states may trigger
        // a change event.
        final PythonVersion pythonVersion = m_enforcedVersion != null //
            ? m_enforcedVersion //
            : config.getPythonVersion();
        m_python2Button.setSelected(pythonVersion == PythonVersion.PYTHON2);
        m_python3Button.setSelected(pythonVersion == PythonVersion.PYTHON3);
    }

    /**
     * Save this panel's settings to the given configuration.
     *
     * @param config The configuration.
     */
    public void saveSettingsTo(final PythonSourceCodeConfig config) {
        final PythonVersion pythonVersion = getPythonVersion();
        config.setPythonVersion(pythonVersion);
        config.setPython2Command(m_python2Command);
        config.setPython3Command(m_python3Command);
    }
}
