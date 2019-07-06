package com.yajad.jmeter.gui;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DubboCommonPanel {
    private JComboBox<String> registryProtocol;
    private JLabeledTextField registryAddress;
    private JLabeledTextField registryGroup;
    private JComboBox<String> rpcProtocol;
    private JLabeledTextField serviceTimeout;
    private JLabeledTextField serviceRetries;
    private JLabeledTextField serviceVersion;
    private JComboBox<String> serviceCluster;
    private JLabeledTextField serviceGroup;
    private JLabeledTextField serviceConnections;
    private JComboBox<String> serviceLoadBalance;

    void clearFields() {
        registryProtocol.setSelectedItem("");
        registryAddress.setText("");
        registryGroup.setText("");
        rpcProtocol.setSelectedItem("");
        serviceTimeout.setText("");
        serviceRetries.setText("");
        serviceVersion.setText("");
        serviceCluster.setSelectedItem("");
        serviceGroup.setText("");
        serviceConnections.setText("");
        serviceLoadBalance.setSelectedItem("");
    }

    public void resetFieldsToDefault() {
        clearFields();

        registryProtocol.removeItem("");
        rpcProtocol.removeItem("");
        serviceCluster.removeItem("");
        serviceLoadBalance.removeItem("");

        registryProtocol.setSelectedItem("zookeeper");
        rpcProtocol.setSelectedItem("dubbo");
        serviceTimeout.setText("120000");
        serviceRetries.setText("0");
        serviceCluster.setSelectedItem("failfast");
        serviceConnections.setText("100");
        serviceLoadBalance.setSelectedItem("random");
    }

    public void configure(TestElement testElement) {
        registryProtocol.setSelectedItem(testElement.getPropertyAsString(DubboElement.REGISTRY_PROTOCOL));
        registryAddress.setText(testElement.getPropertyAsString(DubboElement.REGISTRY_ADDRESS));
        registryGroup.setText(testElement.getPropertyAsString(DubboElement.REGISTRY_GROUP));
        rpcProtocol.setSelectedItem(testElement.getPropertyAsString(DubboElement.RPC_PROTOCOL));
        serviceTimeout.setText(testElement.getPropertyAsString(DubboElement.SERVICE_TIMEOUT));
        serviceRetries.setText(testElement.getPropertyAsString(DubboElement.SERVICE_RETRIES));
        serviceVersion.setText(testElement.getPropertyAsString(DubboElement.SERVICE_VERSION));
        serviceCluster.setSelectedItem(testElement.getPropertyAsString(DubboElement.SERVICE_CLUSTER));
        serviceGroup.setText(testElement.getPropertyAsString(DubboElement.SERVICE_GROUP));
        serviceConnections.setText(testElement.getPropertyAsString(DubboElement.SERVICE_CONNECTIONS));
        serviceLoadBalance.setSelectedItem(testElement.getPropertyAsString(DubboElement.SERVICE_LOAD_BALANCE));
    }

    public void modifyTestElement(TestElement testElement) {
        testElement.setProperty(DubboElement.REGISTRY_PROTOCOL, Objects.requireNonNull(registryProtocol.getSelectedItem()).toString());
        testElement.setProperty(DubboElement.REGISTRY_ADDRESS, registryAddress.getText());
        testElement.setProperty(DubboElement.REGISTRY_GROUP, registryGroup.getText());
        testElement.setProperty(DubboElement.RPC_PROTOCOL, Objects.requireNonNull(rpcProtocol.getSelectedItem()).toString());
        testElement.setProperty(DubboElement.SERVICE_TIMEOUT, serviceTimeout.getText());
        testElement.setProperty(DubboElement.SERVICE_RETRIES, serviceRetries.getText());
        testElement.setProperty(DubboElement.SERVICE_VERSION, serviceVersion.getText());
        testElement.setProperty(DubboElement.SERVICE_CLUSTER, Objects.requireNonNull(serviceCluster.getSelectedItem()).toString());
        testElement.setProperty(DubboElement.SERVICE_GROUP, serviceGroup.getText());
        testElement.setProperty(DubboElement.SERVICE_CONNECTIONS, serviceConnections.getText());
        testElement.setProperty(DubboElement.SERVICE_LOAD_BALANCE, Objects.requireNonNull(serviceLoadBalance.getSelectedItem()).toString());
    }

    public JPanel makeRegistryPanel() {
        JPanel registryPanel = new JPanel();
        registryPanel.setLayout(new WrapLayout(FlowLayout.LEADING, 5, 5));
        registryPanel.setBorder(BorderFactory.createTitledBorder("Registry"));

        registryPanel.add(new JLabel("Protocol: "));
        registryPanel.add(registryProtocol = new JComboBox<>(new String[]{"", "zookeeper", "none", "multicast", "redis", "simple"}));
        registryPanel.add(registryAddress = new JLabeledTextField("Address: ", 20));
        registryPanel.add(registryGroup = new JLabeledTextField("Group: ", 6));
        return registryPanel;
    }

    public JPanel makeServicePanel() {
        JPanel servicePanel = new JPanel();
        servicePanel.setLayout(new WrapLayout(FlowLayout.LEADING, 5, 5));
        servicePanel.setBorder(BorderFactory.createTitledBorder("Service"));

        servicePanel.add(new JLabel("Protocol: "));
        servicePanel.add(rpcProtocol = new JComboBox<>(new String[]{"", "dubbo", "rmi", "hessian", "webservice", "memcached", "redis"}));
        servicePanel.add(serviceTimeout = new JLabeledTextField("Timeout (milliseconds): ", 6));
        servicePanel.add(serviceRetries = new JLabeledTextField("Retries: ", 6));
        servicePanel.add(serviceVersion = new JLabeledTextField("Version: ", 6));
        servicePanel.add(new JLabel("Cluster: "));
        servicePanel.add(serviceCluster = new JComboBox<>(new String[]{"", "failover", "failsafe", "failfast", "failback", "forking"}));
        servicePanel.add(serviceGroup = new JLabeledTextField("Group: ", 6));
        servicePanel.add(serviceConnections = new JLabeledTextField("Connections: ", 6));
        servicePanel.add(new JLabel("Load Balance: "));
        servicePanel.add(serviceLoadBalance = new JComboBox<>(new String[]{"", "random", "roundrobin", "leastactive", "consistenthash"}));
        return servicePanel;
    }
}