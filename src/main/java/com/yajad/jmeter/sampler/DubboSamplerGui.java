package com.yajad.jmeter.sampler;

import com.yajad.jmeter.gui.DubboPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import java.awt.*;

public class DubboSamplerGui extends AbstractSamplerGui {
	private final DubboPanel panel;

	public DubboSamplerGui() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());

		add(makeTitlePanel(), BorderLayout.NORTH);

		panel = new DubboPanel();
		add(panel.init(), BorderLayout.CENTER);
		panel.initFields();
	}

	@Override
	public String getStaticLabel() {
		return "Yajad Dubbo Sampler";
	}

	@Override
	public String getLabelResource() {
		return this.getClass().getSimpleName();
	}

	@Override
	public TestElement createTestElement() {
		DubboSampler sampler = new DubboSampler();
		modifyTestElement(sampler);
		return sampler;
	}

	@Override
	public void configure(TestElement element) {
		super.configure(element);
		panel.configure(element);
	}

	@Override
	public void modifyTestElement(TestElement testElement) {
		super.configureTestElement(testElement);
		panel.modifyTestElement(testElement);
	}

	@Override
	public void clearGui() {
		super.clearGui();
		panel.initFields();
	}
}
