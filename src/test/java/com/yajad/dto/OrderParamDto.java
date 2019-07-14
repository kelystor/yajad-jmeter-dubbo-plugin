package com.yajad.dto;

import java.io.Serializable;

public class OrderParamDto implements Serializable {
    private String id;
    private Integer price;
    private PageRequest page;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public PageRequest getPage() {
        return page;
    }

    public void setPage(PageRequest page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "OrderParamDto{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", page=" + page +
                '}';
    }
}
