package com.cooler.semantic.component.data;

import java.util.List;

public class DataBean1 {
    private int id = 1;
    private String name = "aaa";
    private List<String> data;

    public DataBean1() {
    }

    public DataBean1(int id, String name, List<String> data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataBean1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
