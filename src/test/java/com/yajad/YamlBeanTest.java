package com.yajad;

import com.yajad.dto.OrderParamDto;
import com.yajad.jmeter.dto.DubboParamDto;
import com.yajad.jmeter.parse.YamlParamParser;
import org.junit.Assert;
import org.junit.Test;

public class YamlBeanTest {
    @Test
    public void bean() {
    String yaml = FileUtils.read("order.yaml");
        DubboParamDto dubboParamDto = YamlParamParser.parseParameter(yaml);
        Assert.assertTrue(dubboParamDto.getValues().get(0) instanceof OrderParamDto);
    }
}
