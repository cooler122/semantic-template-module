package com.cooler.semantic.component.data;

public class DataComponentBase<T> implements DataComponent<T> {
    private String id;
    private String type;
    private T data;

    public DataComponentBase() {
    }

    public DataComponentBase(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public DataComponentBase(String id, String type, T data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
