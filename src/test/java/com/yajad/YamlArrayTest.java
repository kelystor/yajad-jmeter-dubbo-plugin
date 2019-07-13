package com.yajad;

import com.yajad.jmeter.dto.DubboParamDto;
import com.yajad.jmeter.parse.YamlParamParser;
import org.junit.Assert;
import org.junit.Test;

public class YamlArrayTest {
    @Test
    public void array() {
        String yaml = FileUtils.read("array.yaml");
        DubboParamDto dubboParamDto = YamlParamParser.parseParameter(yaml);

        Integer[] integerArray = new Integer[]{1, 2, 3};
        Assert.assertEquals(toArrayType(Integer.class), dubboParamDto.getTypes().get(0));
        Assert.assertArrayEquals(integerArray, (Object[]) dubboParamDto.getValues().get(0));
        Assert.assertEquals(toArrayType(Number.class), dubboParamDto.getTypes().get(1));
        Assert.assertArrayEquals(integerArray, (Object[]) dubboParamDto.getValues().get(1));
        Assert.assertEquals(Integer[].class.getName(), dubboParamDto.getTypes().get(2));
        Assert.assertArrayEquals(integerArray, (Object[]) dubboParamDto.getValues().get(2));
    }

    private static String toArrayType(Class<?> clazz) {
        return clazz.getName() + "[]";
    }
}
