package com.cooler.semantic.component.data;

import com.cooler.semantic.model.ContextOwner;

public class DataComponentBase<T> implements DataComponent<T> {
    /**
     * 数据组件ID（流程图中的简称）
     */
    protected String id;
    /**
     * 瞬时数据归属者（accountId、userId、contextId信息)
     */
    protected ContextOwner contextOwner;
    /**
     * 数据组件类型（这个组件的短类名）
     */
    protected String type;
    /**
     * 数据组件数据体
     */
    protected T data;

    public DataComponentBase() {
    }

//    public DataComponentBase(String id, ContextOwner contextOwner, String type) {
//        this.id = id;
//        this.contextOwner = contextOwner;
//        this.type = type;
//    }


    public DataComponentBase(String id, ContextOwner contextOwner, String type, T data) {
        this.id = id;
        this.contextOwner = contextOwner;
        this.type = type;
        this.data = data;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public ContextOwner getContextOwner() {
        return contextOwner;
    }

    public void setContextOwner(ContextOwner contextOwner) {
        this.contextOwner = contextOwner;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataComponentBase{" +
                "id='" + id + '\'' +
                ", contextOwner=" + contextOwner +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
