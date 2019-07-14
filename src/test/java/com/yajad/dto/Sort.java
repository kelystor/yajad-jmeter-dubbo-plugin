package com.yajad.dto;

public class Sort {
    private String field;
    private String order;

    @Override
    public String toString() {
        return "Sort{" +
                "field='" + field + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
