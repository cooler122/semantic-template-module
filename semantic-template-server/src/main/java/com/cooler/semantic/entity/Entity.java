package com.cooler.semantic.entity;

import java.util.Date;

public class Entity {
    private Integer id;

    private String entityName;

    private Integer entityType;

    private Integer accountId;

    private Integer state;

    private String entityMsg;

    private Date createTime;

    private Date updateTime;

    public Entity(Integer id, String entityName, Integer entityType, Integer accountId, Integer state, String entityMsg, Date createTime, Date updateTime) {
        this.id = id;
        this.entityName = entityName;
        this.entityType = entityType;
        this.accountId = accountId;
        this.state = state;
        this.entityMsg = entityMsg;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Entity() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName == null ? null : entityName.trim();
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
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

    public String getEntityMsg() {
        return entityMsg;
    }

    public void setEntityMsg(String entityMsg) {
        this.entityMsg = entityMsg == null ? null : entityMsg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}