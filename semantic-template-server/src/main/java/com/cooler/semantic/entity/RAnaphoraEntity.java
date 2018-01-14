package com.cooler.semantic.entity;

import java.util.Date;

public class RAnaphoraEntity {
    private Integer id;

    private Integer entityId;

    private String entityName;

    private Integer referredEntityId;

    private String referredEntityName;

    private Byte hasConstraint;

    private Integer accountId;

    private Integer state;

    private Date createTime;

    public RAnaphoraEntity(Integer id, Integer entityId, String entityName, Integer referredEntityId, String referredEntityName, Byte hasConstraint, Integer accountId, Integer state, Date createTime) {
        this.id = id;
        this.entityId = entityId;
        this.entityName = entityName;
        this.referredEntityId = referredEntityId;
        this.referredEntityName = referredEntityName;
        this.hasConstraint = hasConstraint;
        this.accountId = accountId;
        this.state = state;
        this.createTime = createTime;
    }

    public RAnaphoraEntity() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName == null ? null : entityName.trim();
    }

    public Integer getReferredEntityId() {
        return referredEntityId;
    }

    public void setReferredEntityId(Integer referredEntityId) {
        this.referredEntityId = referredEntityId;
    }

    public String getReferredEntityName() {
        return referredEntityName;
    }

    public void setReferredEntityName(String referredEntityName) {
        this.referredEntityName = referredEntityName == null ? null : referredEntityName.trim();
    }

    public Byte getHasConstraint() {
        return hasConstraint;
    }

    public void setHasConstraint(Byte hasConstraint) {
        this.hasConstraint = hasConstraint;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}