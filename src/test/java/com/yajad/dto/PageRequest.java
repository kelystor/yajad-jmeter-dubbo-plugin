package com.yajad.dto;

import java.io.Serializable;

public class PageRequest implements Serializable {
    public int page;
    public int rows;
    private Sort sort;

    @Override
    public String toString() {
        return "PageRequest{" +
                "page=" + page +
                ", rows=" + rows +
                ", sort=" + sort +
                '}';
    }
}
