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
package org.knime.python2.generic;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.node.workflow.WorkflowManager.NodeModelFilter;
import org.knime.core.util.UniqueNameGenerator;

/**
 *
 * @author wiswedel
 */
public class PythonEnvironment {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(PythonEnvironment.class);

    private String m_name;
    private JsonObject m_definition;

    /**
     *
     */
    private PythonEnvironment(final String name, final JsonObject definition) {
        m_name = name;
        m_definition = definition;

    }

    public String getDefinitionAsString() {
        return writeAsString(m_definition);
    }

    public String getName() {
        return m_name;
    }

    public JsonObject getDefinition() {
        return m_definition;
    }

    /**
     * @param settings
     */
    public void save(final NodeSettingsWO settings) {
        NodeSettingsWO pythonEnvSettings = settings.addNodeSettings("pythonEnvironment");
        pythonEnvSettings.addString("name", m_name);
        pythonEnvSettings.addTransientString("definition", writeAsString(m_definition));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_definition == null) ? 0 : m_definition.hashCode());
        result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PythonEnvironment other = (PythonEnvironment)obj;
        if (!Objects.equals(m_definition, other.m_definition)) {
            return false;
        }
        if (!Objects.equals(m_name, other.m_name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return m_name + "\n----------------------\n" + getDefinitionAsString();
    }

    public static PythonEnvironment load(final String name, final JsonObject json){
        return new PythonEnvironment(name, json);
    }

    public static JsonObject readAsJsonObject(final String definitionJsonString) throws InvalidSettingsException {
        try (JsonReader jsonReader = Json.createReader(new StringReader(definitionJsonString))) {
            return jsonReader.readObject();
        }
    }

    public static String writeAsString(final JsonObject jsonObject) {
        try (StringWriter w = new StringWriter(); JsonWriter jsonWriter = Json.createWriter(w)) {
            jsonWriter.writeObject(jsonObject);
            return w.toString(); // häh, should this be closed before return?
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static void validate(final NodeSettingsRO settings) throws InvalidSettingsException {
        if (settings.containsKey("pythonEnvironment")) {
            NodeSettingsRO pythonEnvSettings = settings.getNodeSettings("pythonEnvironment");
            String name = pythonEnvSettings.getString("name");
            String defintion = pythonEnvSettings.getTransientString("definition");
            // TODO validation?
        }
    }

    /**
     * @param settings
     * @return
     * @throws InvalidSettingsException
     */
    public static <T extends NodeModel & PythonEnvironmentAware> PythonEnvironment loadInModel(final T model, final NodeSettingsRO settings) throws InvalidSettingsException {
        if (settings.containsKey("pythonEnvironment")) {
            NodeSettingsRO pythonEnvSettings = settings.getNodeSettings("pythonEnvironment");
            String name = pythonEnvSettings.getString("name");
            String definitionAsString = pythonEnvSettings.getTransientString("definition");
            List<PythonEnvironment> allEnvsInWorkflow = getAllEnvironmentsInCurrentWorkflow(null);
            Optional<PythonEnvironment> matchOnWorkflow = allEnvsInWorkflow.stream().filter(e -> e.getName().equals(name)).findFirst();
            final PythonEnvironment finalEnvironment;

            if (definitionAsString != null) {
                JsonObject def = PythonEnvironment.readAsJsonObject(definitionAsString);
                if (matchOnWorkflow.isPresent()) {
                    if (!def.equals(matchOnWorkflow.get().getDefinition())) {
                        UniqueNameGenerator nameGen = new UniqueNameGenerator(
                            allEnvsInWorkflow.stream().map(PythonEnvironment::getName).collect(Collectors.toSet()));
                        String uniqueName = nameGen.newName(name);
                        model.setWarning(String.format(
                            "Python Environment with same name (\"%s\") already defined on workflow -- will use different name (\"%s\")",
                            name, uniqueName));
                        finalEnvironment = PythonEnvironment.load(uniqueName, def);
                    } else {
                        finalEnvironment = matchOnWorkflow.get();
                    }
                } else {
                    finalEnvironment = PythonEnvironment.load(name, def);
                }
            } else {
                if (matchOnWorkflow.isPresent()) {
                    finalEnvironment = matchOnWorkflow.get();
                } else {
                    // boah, what is it? env needs to be loaded from <workflow>/.artifacts?
                    finalEnvironment = createNewEnvironment(name, null);
                }
            }
            return finalEnvironment;
        }
        return null;
    }

    /**
     * @param name
     * @param definition TODO
     * @return
     */
    private static PythonEnvironment createNewEnvironment(final String name, final String definition) {
        JsonObject jsonDefinition = Json.createObjectBuilder().add("some value", "auto-generated").build();
        if (definition != null) {
            try {
                jsonDefinition = PythonEnvironment.readAsJsonObject(definition);
            } catch (InvalidSettingsException ex) {

            }
        }
        return PythonEnvironment.load(name, jsonDefinition);
    }

    public static PythonEnvironment loadInDialog(final NodeSettingsRO settings) {
        List<PythonEnvironment> allEnvs = getAllEnvironmentsInCurrentWorkflow(null);
        if (settings.containsKey("pythonEnvironment")) {
            NodeSettingsRO pythonEnvSettings;
            try {
                pythonEnvSettings = settings.getNodeSettings("pythonEnvironment");
                String name = pythonEnvSettings.getString("name", "new environment");
                String definitionAsString = pythonEnvSettings.getTransientString("definition");
                return allEnvs.stream().filter(env -> env.getName().equals(name)).findFirst().orElseGet(() -> createNewEnvironment(name, definitionAsString));
            } catch (InvalidSettingsException ex) {
                // ignore
            }
        }
        return allEnvs.stream().findFirst().orElseGet(() -> createNewEnvironment("new environment", null));
    }

    public static List<PythonEnvironment> getAllEnvironmentsInCurrentWorkflow(final WorkflowManager workflow) {
        WorkflowManager wfm;
        if (workflow == null) {
            NodeContext context = NodeContext.getContext();
            if (context == null || context.getWorkflowManager() == null) {
                LOGGER.errorWithFormat(
                        "Could not collect python environments used in workflow (no access to workflow manager)");
                return Collections.emptyList();
            }
            wfm = context.getWorkflowManager();
        } else {
            wfm = workflow;
        }
        Map<NodeID, NodeModel> pythonNodes =
                wfm.findNodes(NodeModel.class, new NodeModelFilter<NodeModel>() {
                    @Override
                    public boolean include(final NodeModel nodeModel) {
                        return nodeModel instanceof PythonEnvironmentAware;
                    }
                }, true, true);
        return pythonNodes.values().stream().map(PythonEnvironmentAware.class::cast)
                .map(PythonEnvironmentAware::getPythonEnvironment).filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
    }

}
