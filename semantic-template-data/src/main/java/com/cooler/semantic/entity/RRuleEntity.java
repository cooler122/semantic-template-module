package com.cooler.semantic.entity;

import java.io.Serializable;
import java.util.Date;

public class RRuleEntity implements Serializable{
    private Integer id;

    private Integer ruleId;

    private String ruleName;

    private Integer serialNumber;

    private Integer entityType;

    private Integer entityId;

    private String entityTypeId;

    private String entityName;

    private String entityAppName;

    private Integer queryType;

    private String necessaryEntityQuery;

    private String natrue;

    private Double volumeRate;

    private Double weight;

    private Integer state;

    private Integer accountId;

    private Date createTime;

    public RRuleEntity(Integer id, Integer ruleId, String ruleName, Integer serialNumber, Integer entityType, Integer entityId, String entityTypeId, String entityName, String entityAppName, Integer queryType, String necessaryEntityQuery, String natrue, Double volumeRate, Double weight, Integer state, Integer accountId, Date createTime) {
        this.id = id;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.serialNumber = serialNumber;
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityTypeId = entityTypeId;
        this.entityName = entityName;
        this.entityAppName = entityAppName;
        this.queryType = queryType;
        this.necessaryEntityQuery = necessaryEntityQuery;
        this.natrue = natrue;
        this.volumeRate = volumeRate;
        this.weight = weight;
        this.state = state;
        this.accountId = accountId;
        this.createTime = createTime;
    }

    public RRuleEntity() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(String entityTypeId) {
        this.entityTypeId = entityTypeId == null ? null : entityTypeId.trim();
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName == null ? null : entityName.trim();
    }

    public String getEntityAppName() {
        return entityAppName;
    }

    public void setEntityAppName(String entityAppName) {
        this.entityAppName = entityAppName == null ? null : entityAppName.trim();
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getNecessaryEntityQuery() {
        return necessaryEntityQuery;
    }

    public void setNecessaryEntityQuery(String necessaryEntityQuery) {
        this.necessaryEntityQuery = necessaryEntityQuery == null ? null : necessaryEntityQuery.trim();
    }

    public String getNatrue() {
        return natrue;
    }

    public void setNatrue(String natrue) {
        this.natrue = natrue == null ? null : natrue.trim();
    }

    public Double getVolumeRate() {
        return volumeRate;
    }

    public void setVolumeRate(Double volumeRate) {
        this.volumeRate = volumeRate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}