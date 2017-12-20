package com.cooler.semantic.component.data;

public class DataBean3 {
    private int id = 3;
    private String name = "ccc";
    private String data;

    public DataBean3() {
    }

    public DataBean3(int id, String name, String data) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataBean3{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
