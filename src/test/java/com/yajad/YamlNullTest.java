package com.yajad;

import com.yajad.jmeter.dto.DubboParamDto;
import com.yajad.jmeter.parse.YamlParamParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class YamlNullTest {
    @Test
    public void nullType() {
        String yaml = FileUtils.read("null.yaml");
        DubboParamDto dubboParamDto = YamlParamParser.parseParameter(yaml);

        Assert.assertNull(dubboParamDto.getValues().get(0));
        Assert.assertNull(dubboParamDto.getValues().get(1));
        Assert.assertEquals(Integer.class.getName(), dubboParamDto.getTypes().get(0));
        Assert.assertEquals(StringUtils.class.getName(), dubboParamDto.getTypes().get(1));
        Assert.assertEquals(Float.class.getName(), dubboParamDto.getTypes().get(2));
        Assert.assertEquals(Double.class.getName(), dubboParamDto.getTypes().get(3));
    }
}
