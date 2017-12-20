package com.cooler.semantic.component.data;

public class DataComponentBase<T> implements DataComponent<T> {
    /**
     * 数据组件ID
     */
    private String id;
    /**
     * 数据组件类型（1为功能组件，2为逻辑组件）
     */
    private String type;
    /**
     * 数据组件数据体
     */
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

    @Override
    public String toString() {
        return "DataComponentBase{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
