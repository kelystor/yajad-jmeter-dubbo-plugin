package com.yajad;

import com.yajad.jmeter.sampler.DubboSampler;
import com.yajad.jmeter.sampler.DubboSamplerGui;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class DubboSamplerGuiTest {
	@Test
	public void displayGUI() throws InterruptedException {
		DubboSamplerGui samplerGui = new DubboSamplerGui();
		DubboSampler te = (DubboSampler) samplerGui.createTestElement();
		samplerGui.configure(te);
		samplerGui.clearGui();
		samplerGui.modifyTestElement(te);

		JFrame frame = new JFrame(samplerGui.getStaticLabel());

		frame.setPreferredSize(new Dimension(1024, 768));
		frame.getContentPane().add(samplerGui, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		while (frame.isVisible()) {
			Thread.sleep(1000);
		}
	}
}
