package com.yajad.jmeter.dto;

import java.util.List;

public class DubboParamDto {
    private List<String> types;
    private List<Object> values;

    public DubboParamDto(List<String> types, List<Object> values) {
        this.types = types;
        this.values = values;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "DubboParamDto{" +
                "types=" + types +
                ", values=" + values +
                '}';
    }
}
