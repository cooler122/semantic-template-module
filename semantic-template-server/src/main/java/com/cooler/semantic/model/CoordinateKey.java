package com.cooler.semantic.model;

import java.util.Objects;

/**
 * Created by zhangsheng on 2018/1/14.
 */
public class CoordinateKey {
    /**
     * 上下文编号
     */
    private Integer contextId;

    /**
     * 实体类型
     */
    private Integer entityType;

    /**
     * 实体ID
     */
    private Integer entityId;

    public Integer getContextId() {
        return contextId;
    }

    public void setContextId(Integer contextId) {
        this.contextId = contextId;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public CoordinateKey(Integer contextId, Integer entityType, Integer entityId) {
        this.contextId = contextId;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateKey that = (CoordinateKey) o;
        return Objects.equals(entityType, that.entityType) &&
                Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contextId, entityType, entityId);
    }
}
