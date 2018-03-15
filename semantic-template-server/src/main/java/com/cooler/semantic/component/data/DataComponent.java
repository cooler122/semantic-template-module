package com.cooler.semantic.component.data;

import com.cooler.semantic.model.ContextOwner;

import java.io.Serializable;

public interface DataComponent<T> extends Serializable{
    /**
     * 获取此数据组件的ID
     * @return
     */
    String getId();

    /**
     * 设置此数据组件的ID
     * @param id
     */
    void setId(String id);

    /**
     * 获取数据组件的用户（accountId、userId、contextId)
     * @return
     */
    ContextOwner getContextOwner();

    /**
     * 设置数据组件的用户（accountId、userId、contextId)
     * @param contextOwner
     */
    void setContextOwner(ContextOwner contextOwner);

    /**
     * 获取此数据组件的类型
     * @return
     */
    String getType();

    /**
     * 设置此数据组件的类型
     * @param type
     */
    void setType(String type);

    /**
     * 获取此数据组件的数据体
     * @return
     */
    T getData();

    /**
     * 设置此数据组件的数据体
     * @param data
     */
    void setData(T data);
}
