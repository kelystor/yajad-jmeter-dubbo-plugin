package com.yajad.jmeter.sampler;

import com.yajad.jmeter.gui.DubboElement;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;

public class DubboSampler extends AbstractSampler implements Interruptible {
	private final DubboElement element;

	public DubboSampler() {
		this.element = new DubboElement(this);
	}

	@Override
	public boolean interrupt() {
		Thread.currentThread().interrupt();
		return true;
	}

	@Override
	public SampleResult sample(Entry e) {
		return element.sample();
	}

	public DubboElement getElement() {
		return element;
	}

}
