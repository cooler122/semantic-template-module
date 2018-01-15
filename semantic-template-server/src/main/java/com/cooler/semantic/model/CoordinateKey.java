package com.cooler.semantic.model;

import java.util.Objects;

/**
 * Created by zhangsheng on 2018/1/14.
 */
public class CoordinateKey {
    /**
     * 句子向量编号
     */
    private Integer sentenceVectorId;

    /**
     * 实体类型
     */
    private Integer entityType;

    /**
     * 实体ID
     */
    private Integer entityId;

    public Integer getSentenceVectorId() {
        return sentenceVectorId;
    }

    public void setSentenceVectorId(Integer sentenceVectorId) {
        this.sentenceVectorId = sentenceVectorId;
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

    public CoordinateKey(Integer sentenceVectorId, Integer entityType, Integer entityId) {
        this.sentenceVectorId = sentenceVectorId;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateKey that = (CoordinateKey) o;
        return Objects.equals(sentenceVectorId, that.sentenceVectorId) &&
                Objects.equals(entityType, that.entityType) &&
                Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentenceVectorId, entityType, entityId);
    }
}
