package com.yajad.jmeter.gui;

import com.yajad.jmeter.sampler.DubboSampler;
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
import java.util.Objects;

public class DubboPanel extends JPanel {
	private JComboBox<String> registryProtocol;
	private JTextField registryAddress;
	private JTextField registryGroup;
	private JComboBox<String> rpcProtocol;
	private JTextField serviceTimeout;
	private JTextField serviceRetries;
	private JTextField serviceVersion;
	private JComboBox<String> serviceCluster;
	private JTextField serviceGroup;
	private JTextField serviceConnections;
	private JComboBox<String> serviceLoadBalance;
	private JTextField serviceInterface;
	private JTextField serviceMethod;
	private JTextArea serviceParameter;

	public DubboPanel() {
		super(new GridBagLayout());
	}

	public void initFields() {
		registryProtocol.setSelectedItem("zookeeper");
		registryAddress.setText("${dubboRegistryAddress}");
		registryGroup.setText("");
		rpcProtocol.setSelectedItem("dubbo");
		serviceTimeout.setText("120000");
		serviceRetries.setText("0");
		serviceVersion.setText("");
		serviceCluster.setSelectedItem("failfast");
		serviceGroup.setText("");
		serviceConnections.setText("100");
		serviceLoadBalance.setSelectedItem("random");
		serviceInterface.setText("");
		serviceMethod.setText("");
		serviceParameter.setText("");
	}

	public JPanel init() {
		JPanel container = new VerticalPanel(5, 0);

		JPanel registryPanel = new JPanel();
		registryPanel.setLayout(new WrapLayout(FlowLayout.LEADING,5,5));
		registryPanel.setBorder(BorderFactory.createTitledBorder("Registry"));

		registryPanel.add(new JLabel("Protocol: ", JLabel.RIGHT));
		registryPanel.add(registryProtocol = new JComboBox<>(new String[]{"zookeeper", "none", "multicast", "redis", "simple"}));
		registryPanel.add(new JLabel("Address: ", JLabel.RIGHT));
		registryPanel.add(registryAddress = new JTextField(20));
		registryPanel.add(new JLabel("Group: ", JLabel.RIGHT));
		registryPanel.add(registryGroup = new JTextField(6));

		container.add(registryPanel);

		JPanel servicePanel = new JPanel();
		servicePanel.setLayout(new WrapLayout(FlowLayout.LEADING,5,5));
		servicePanel.setBorder(BorderFactory.createTitledBorder("Service"));

		servicePanel.add(new JLabel("Protocol: ", JLabel.RIGHT));
		servicePanel.add(rpcProtocol = new JComboBox<>(new String[]{"dubbo"}));
		servicePanel.add(new JLabel("Timeout (milliseconds): ", JLabel.RIGHT));
		servicePanel.add(serviceTimeout = new JTextField(6));
		servicePanel.add(new JLabel("Retries: ", JLabel.RIGHT));
		servicePanel.add(serviceRetries = new JTextField(6));
		servicePanel.add(new JLabel("Version: ", JLabel.RIGHT));
		servicePanel.add(serviceVersion = new JTextField(6));
		servicePanel.add(new JLabel("Cluster: ", JLabel.RIGHT));
		servicePanel.add(serviceCluster = new JComboBox<>(new String[]{"failover", "failsafe", "failfast", "failback", "forking"}));
		servicePanel.add(new JLabel("Group: ", JLabel.RIGHT));
		servicePanel.add(serviceGroup = new JTextField(6));
		servicePanel.add(new JLabel("Connections: ", JLabel.RIGHT));
		servicePanel.add(serviceConnections = new JTextField(6));
		servicePanel.add(new JLabel("Load Balance: ", JLabel.RIGHT));
		servicePanel.add(serviceLoadBalance = new JComboBox<>(new String[]{"random", "roundrobin", "leastactive", "consistenthash"}));

        container.add(servicePanel);

        JPanel gridPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(gridPanel, labelConstraints, 0, 0, new JLabel("Interface: ", JLabel.RIGHT));
        addToPanel(gridPanel, editConstraints, 1, 0, serviceInterface = new JTextField(20));
        addToPanel(gridPanel, labelConstraints, 0, 1, new JLabel("Method: ", JLabel.RIGHT));
        addToPanel(gridPanel, editConstraints, 1, 1, serviceMethod = new JTextField(20));

        addToPanel(gridPanel, labelConstraints, 0, 2, new JLabel("Parameter (yaml): ", JLabel.RIGHT));
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
                JOptionPane.showMessageDialog(this.getParent(), "parameter is empty", "error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!JsonUtils.validate(parameter)) {
                JOptionPane.showMessageDialog(this.getParent(), "invalid json", "error", JOptionPane.ERROR_MESSAGE);
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

        JLabel link = new JLabel("YAJAD JMeter Apache Dubbo Plugin", JLabel.RIGHT);
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
        addToPanel(gridPanel, labelConstraints, 1, 4, new JLabel("Powered by 小龙虾", JLabel.RIGHT));

        container.add(gridPanel);

		return container;
	}

	public void configure(TestElement element) {
		registryProtocol.setSelectedItem(element.getPropertyAsString(DubboElement.REGISTRY_PROTOCOL));
		registryAddress.setText(element.getPropertyAsString(DubboElement.REGISTRY_ADDRESS));
		registryGroup.setText(element.getPropertyAsString(DubboElement.REGISTRY_GROUP));
		rpcProtocol.setSelectedItem(element.getPropertyAsString(DubboElement.RPC_PROTOCOL));
		serviceTimeout.setText(element.getPropertyAsString(DubboElement.SERVICE_TIMEOUT));
		serviceRetries.setText(element.getPropertyAsString(DubboElement.SERVICE_RETRIES));
		serviceVersion.setText(element.getPropertyAsString(DubboElement.SERVICE_VERSION));
		serviceCluster.setSelectedItem(element.getPropertyAsString(DubboElement.SERVICE_CLUSTER));
		serviceGroup.setText(element.getPropertyAsString(DubboElement.SERVICE_GROUP));
		serviceConnections.setText(element.getPropertyAsString(DubboElement.SERVICE_CONNECTIONS));
		serviceLoadBalance.setSelectedItem(element.getPropertyAsString(DubboElement.SERVICE_LOAD_BALANCE));
		serviceInterface.setText(element.getPropertyAsString(DubboElement.SERVICE_INTERFACE));
		serviceMethod.setText(element.getPropertyAsString(DubboElement.SERVICE_METHOD));
		serviceParameter.setText(element.getPropertyAsString(DubboElement.SERVICE_PARAMETER));
	}

	public void modifyTestElement(TestElement te) {
		DubboElement dubboElement = ((DubboSampler) te).getElement();
		dubboElement.setRegistryProtocol(Objects.requireNonNull(registryProtocol.getSelectedItem()).toString());
		dubboElement.setRegistryAddress(registryAddress.getText());
		dubboElement.setRegistryGroup(registryGroup.getText());
		dubboElement.setRpcProtocol(Objects.requireNonNull(rpcProtocol.getSelectedItem()).toString());
		dubboElement.setServiceTimeout(serviceTimeout.getText());
		dubboElement.setServiceRetries(serviceRetries.getText());
		dubboElement.setServiceVersion(serviceVersion.getText());
		dubboElement.setServiceCluster(Objects.requireNonNull(serviceCluster.getSelectedItem()).toString());
		dubboElement.setServiceGroup(serviceGroup.getText());
		dubboElement.setServiceConnections(serviceConnections.getText());
		dubboElement.setServiceLoadBalance(Objects.requireNonNull(serviceLoadBalance.getSelectedItem()).toString());
		dubboElement.setServiceInterface(serviceInterface.getText());
		dubboElement.setServiceMethod(serviceMethod.getText());
		dubboElement.setServiceParameter(serviceParameter.getText());
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
