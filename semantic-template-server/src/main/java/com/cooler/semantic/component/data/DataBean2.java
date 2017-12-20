package com.cooler.semantic.component.data;

import java.util.Map;

public class DataBean2 {
    private int id = 2;
    private String name = "bbb";
    private Map<String, String> data;

    public DataBean2() {
    }

    public DataBean2(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DataBean2(int id, String name, Map<String, String> data) {
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

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataBean2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
