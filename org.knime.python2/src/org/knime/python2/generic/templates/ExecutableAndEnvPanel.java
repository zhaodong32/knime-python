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
 *   Oct 24, 2020 (wiswedel): created
 */
package org.knime.python2.generic.templates;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.util.ViewUtils;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowLock;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.node.workflow.WorkflowManager.NodeModelFilter;
import org.knime.python2.generic.PythonEnvironment;
import org.knime.python2.generic.PythonEnvironmentAware;

/**
 *
 * @author wiswedel
 */
public class ExecutableAndEnvPanel extends JPanel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(ExecutableAndEnvPanel.class);

    private final JComboBox<String> m_envNamesCombo;
    private final JTextArea m_envDefinitionArea;

    private final Class<? extends NodeModel> m_pythonNodeModelClasses;

    private List<PythonEnvironment> m_envsInWorkflow;

    public <T extends NodeModel & PythonEnvironmentAware> ExecutableAndEnvPanel(final Class<T> pythonNodeModelClasses) {
        super(new BorderLayout());
        m_pythonNodeModelClasses = pythonNodeModelClasses;
        m_envNamesCombo = new JComboBox<>(new DefaultComboBoxModel<>());
        m_envNamesCombo.setEditable(true);
        m_envNamesCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                onItemSelected((String)e.getItem());
            }
        });
        m_envDefinitionArea = new JTextArea();
        add(ViewUtils.getInFlowLayout(m_envNamesCombo), BorderLayout.NORTH);
        add(m_envDefinitionArea, BorderLayout.CENTER);
    }

    /**
     *
     */
    private void onItemSelected(final String item) {
        JsonObject definition = m_envsInWorkflow.stream().filter(pe -> pe.getName().equals(item)).findFirst()
            .map(PythonEnvironment::getDefinition)
            .orElseGet(() -> Json.createObjectBuilder().add("content", "newly created").build());
        String asString = "";
        try (StringWriter stringWriter = new StringWriter(); JsonWriter w = Json.createWriter(stringWriter)) {
            w.write(definition);
            asString = stringWriter.toString();
        } catch (IOException ex) {
            LOGGER.error("Unable to write JSON Object to string", ex);
        }
        m_envDefinitionArea.setText(asString);
        long timesUsedInWorkflows = m_envsInWorkflow.stream().filter(pe -> pe.getName().equals(item)).count();
        m_envDefinitionArea.setEditable(timesUsedInWorkflows < 2);
    }

    public void save(final NodeSettingsWO settings) throws InvalidSettingsException {
        String name = m_envNamesCombo.getSelectedItem().toString();
        JsonObject definition = PythonEnvironment.readAsJsonObject(m_envDefinitionArea.getText());
        PythonEnvironment.load(name, definition).save(settings);
    }

    public void load(final NodeSettingsRO settings) {
        NodeContext context = NodeContext.getContext();
        if (context == null || context.getWorkflowManager() == null) {
            LOGGER.warn("No node context -- can't fill environment list from workflow");
            return;
        }
        WorkflowManager wfm = context.getWorkflowManager();
        try (WorkflowLock lock = wfm.lock()) {
            Map<NodeID, ? extends NodeModel> pythonNodesMap =
                wfm.findNodes(m_pythonNodeModelClasses, new NodeModelFilter<>(), true, true);
            m_envsInWorkflow = pythonNodesMap.values().stream().map(PythonEnvironmentAware.class::cast)
                .map(PythonEnvironmentAware::getPythonEnvironment).filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
        }
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)m_envNamesCombo.getModel();
        model.removeAllElements();
        PythonEnvironment env = PythonEnvironment.loadInDialog(settings);
        if (m_envsInWorkflow.stream().noneMatch(e -> e.getName().equals(env.getName()))) {
            m_envsInWorkflow.add(env);
        }
        m_envsInWorkflow.stream().map(PythonEnvironment::getName).distinct().forEach(model::addElement);
        m_envNamesCombo.setSelectedItem(env.getName());
    }

}
