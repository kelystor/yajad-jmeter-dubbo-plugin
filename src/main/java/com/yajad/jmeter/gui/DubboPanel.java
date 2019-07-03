package com.yajad.jmeter.gui;

import com.yajad.jmeter.sampler.DubboSampler;
import org.apache.jmeter.testelement.TestElement;

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
	private JComboBox<String> rpcProtocol;
	private JTextField timeout;
	private JTextField serviceInterface;
	private JTextField method;
	private JTextArea parameter;

	public DubboPanel() {
		super(new GridBagLayout());
	}

	public void initFields() {
		registryProtocol.setSelectedItem("zookeeper");
		registryAddress.setText("${dubboRegistryAddress}");
		rpcProtocol.setSelectedItem("dubbo");
		timeout.setText("120000");
		serviceInterface.setText("");
		method.setText("");
		parameter.setText("");
	}

	public JPanel init() {
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

		GridBagConstraints editConstraints = new GridBagConstraints();
		editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		editConstraints.weightx = 1.0;
		editConstraints.fill = GridBagConstraints.HORIZONTAL;


		addToPanel(this, labelConstraints, 0, 0, new JLabel("Registry Protocol: ", JLabel.RIGHT));
		addToPanel(this, editConstraints, 1, 0, registryProtocol = new JComboBox<>(new String[]{"zookeeper", "none", "multicast", "redis", "simple"}));
		addToPanel(this, labelConstraints, 0, 1, new JLabel("Registry Address: ", JLabel.RIGHT));
		addToPanel(this, editConstraints, 1, 1, registryAddress = new JTextField(20));

		editConstraints.insets = new Insets(2, 0, 0, 0);
		labelConstraints.insets = new Insets(2, 0, 0, 0);

		addToPanel(this, labelConstraints, 0, 2, new JLabel("RPC Protocol: ", JLabel.RIGHT));
		addToPanel(this, editConstraints, 1, 2, rpcProtocol = new JComboBox<>(new String[]{"dubbo"}));
		addToPanel(this, labelConstraints, 0, 3, new JLabel("Timeout (milliseconds): ", JLabel.RIGHT));
		addToPanel(this, editConstraints, 1, 3, timeout = new JTextField(20));
		addToPanel(this, labelConstraints, 0, 4, new JLabel("Service Interface: ", JLabel.RIGHT));
		addToPanel(this, editConstraints, 1, 4, serviceInterface = new JTextField(20));
		addToPanel(this, labelConstraints, 0, 5, new JLabel("Method: ", JLabel.RIGHT));
		addToPanel(this, editConstraints, 1, 5, method = new JTextField(20));

		addToPanel(this, labelConstraints, 0, 6, new JLabel("Parameter (yaml): ", JLabel.RIGHT));
		editConstraints.fill = GridBagConstraints.BOTH;
		parameter = new JTextArea();
		addToPanel(this, editConstraints, 1, 7, createTextAreaScrollPaneContainer(parameter));

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

		addToPanel(this, labelConstraints, 1, 8, link);
		addToPanel(this, labelConstraints, 1, 9, new JLabel("Powered by 小龙虾", JLabel.RIGHT));

		JPanel container = new JPanel(new BorderLayout());
		container.add(this, BorderLayout.NORTH);
		return container;
	}

	public void configure(TestElement element) {
		registryProtocol.setSelectedItem(element.getPropertyAsString(DubboElement.REGISTRY_PROTOCOL));
		registryAddress.setText(element.getPropertyAsString(DubboElement.REGISTRY_ADDRESS));
		rpcProtocol.setSelectedItem(element.getPropertyAsString(DubboElement.RPC_PROTOCOL));
		timeout.setText(element.getPropertyAsString(DubboElement.TIMEOUT));
		serviceInterface.setText(element.getPropertyAsString(DubboElement.SERVICE_INTERFACE));
		method.setText(element.getPropertyAsString(DubboElement.METHOD));
		parameter.setText(element.getPropertyAsString(DubboElement.PARAMETER));
	}

	public void modifyTestElement(TestElement te) {
		DubboElement dubboElement = ((DubboSampler) te).getElement();
		dubboElement.setRegistryProtocol(Objects.requireNonNull(registryProtocol.getSelectedItem()).toString());
		dubboElement.setRegistryAddress(registryAddress.getText());
		dubboElement.setRpcProtocol(Objects.requireNonNull(rpcProtocol.getSelectedItem()).toString());
		dubboElement.setTimeout(timeout.getText());
		dubboElement.setServiceInterface(serviceInterface.getText());
		dubboElement.setMethod(method.getText());
		dubboElement.setParameter(parameter.getText());
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
