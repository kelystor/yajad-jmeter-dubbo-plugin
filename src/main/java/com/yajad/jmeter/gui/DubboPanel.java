package com.yajad.jmeter.gui;

import com.yajad.jmeter.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.snakeyaml.engine.v1.api.*;
import org.snakeyaml.engine.v1.common.FlowStyle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DubboPanel {
    private DubboCommonPanel commonPanel = new DubboCommonPanel();
    private JTextField serviceInterface;
    private JTextField serviceMethod;
    private JTextArea serviceParameter;

    public JPanel init() {
        JPanel container = new VerticalPanel(5, 0);

        container.add(commonPanel.makeRegistryPanel());
        container.add(commonPanel.makeServicePanel());
        container.add(makeParameterPanel());

        clearFields();

        return container;
    }

    public void clearFields() {
        commonPanel.clearFields();

        serviceInterface.setText("");
        serviceMethod.setText("");
        serviceParameter.setText("");
    }

    public void configure(TestElement testElement) {
        commonPanel.configure(testElement);
        serviceInterface.setText(testElement.getPropertyAsString(DubboElement.SERVICE_INTERFACE));
        serviceMethod.setText(testElement.getPropertyAsString(DubboElement.SERVICE_METHOD));
        serviceParameter.setText(testElement.getPropertyAsString(DubboElement.SERVICE_PARAMETER));
    }

    public void modifyTestElement(TestElement testElement) {
        commonPanel.modifyTestElement(testElement);
        testElement.setProperty(DubboElement.SERVICE_INTERFACE, serviceInterface.getText());
        testElement.setProperty(DubboElement.SERVICE_METHOD, serviceMethod.getText());
        testElement.setProperty(DubboElement.SERVICE_PARAMETER, serviceParameter.getText());
    }

    private JPanel makeParameterPanel() {
        JPanel gridPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(gridPanel, labelConstraints, 0, 0, new JLabel("Interface: "));
        addToPanel(gridPanel, editConstraints, 1, 0, serviceInterface = new JTextField(20));
        addToPanel(gridPanel, labelConstraints, 0, 1, new JLabel("Method: "));
        addToPanel(gridPanel, editConstraints, 1, 1, serviceMethod = new JTextField(20));

        addToPanel(gridPanel, labelConstraints, 0, 2, new JLabel("Parameter (yaml): "));
        editConstraints.fill = GridBagConstraints.BOTH;
        serviceParameter = new JTextArea();
        addToPanel(gridPanel, editConstraints, 1, 2, createTextAreaScrollPaneContainer(serviceParameter));

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        buttonConstraints.weightx = 1.0;
        JButton button = new JButton("json to yaml");
        button.addActionListener(e -> {
            String parameter = serviceParameter.getText();
            if (StringUtils.isBlank(parameter)) {
                JOptionPane.showMessageDialog(gridPanel.getParent(), "parameter is empty", "error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!JsonUtils.validate(parameter)) {
                JOptionPane.showMessageDialog(gridPanel.getParent(), "invalid json", "error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LoadSettings settings = new LoadSettingsBuilder().build();
            Load load = new Load(settings);
            Object yamlObject = load.loadFromString(parameter);

            DumpSettings dumpSettings = new DumpSettingsBuilder().setDefaultFlowStyle(FlowStyle.BLOCK).build();
            Dump dump = new Dump(dumpSettings);
            serviceParameter.setText(dump.dumpToString(yamlObject));
        });
        addToPanel(gridPanel, buttonConstraints, 1, 3, button);

        JLabel link = new JLabel("YAJAD JMeter Apache Dubbo Plugin");
        link.setForeground(Color.blue);
        link.setFont(link.getFont().deriveFont(Font.PLAIN));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/kelystor/yajad-jmeter-dubbo-plugin"));
                    } catch (URISyntaxException | IOException ignore) {
                    }
                }
            }
        });
        Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.blue);
        link.setBorder(border);

        addToPanel(gridPanel, labelConstraints, 1, 3, link);
        addToPanel(gridPanel, labelConstraints, 1, 4, new JLabel("Powered by 小龙虾"));

        return gridPanel;
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    private JScrollPane createTextAreaScrollPaneContainer(JTextArea textArea) {
        JScrollPane ret = new JScrollPane();
        textArea.setRows(16);
        textArea.setColumns(20);
        ret.setViewportView(textArea);
        return ret;
    }
}
