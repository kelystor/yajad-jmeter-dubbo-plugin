package com.yajad.jmeter.config;

import com.yajad.jmeter.gui.DubboCommonPanel;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

public class DubboDefaultsGui extends AbstractConfigGui {
    private DubboCommonPanel commonPanel = new DubboCommonPanel();

    public DubboDefaultsGui() {
        super();
        init();
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return "Yajad Dubbo Defaults";
    }

    @Override
    public TestElement createTestElement() {
        ConfigTestElement config = new ConfigTestElement();
        modifyTestElement(config);
        return config;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        super.configureTestElement(testElement);
        commonPanel.modifyTestElement(testElement);
    }

    @Override
    public void configure(TestElement testElement) {
        super.configure(testElement);
        commonPanel.configure(testElement);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        commonPanel.resetFieldsToDefault();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);
        add(makeConfigPanel(), BorderLayout.CENTER);

        commonPanel.resetFieldsToDefault();
    }

    private JPanel makeConfigPanel() {
        JPanel container = new VerticalPanel(5, 0);

        container.add(commonPanel.makeRegistryPanel());
        container.add(commonPanel.makeServicePanel());

        return container;
    }
}
