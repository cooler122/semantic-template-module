package com.cooler.semantic.model;

import java.io.Serializable;

public class MatchedEntityParam implements Serializable{
    /**
     * 规则ID
     */
    private Integer ruleId;

    /**
     * 实体类型
     */
    private Integer entityType;

    /**
     * 实体ID
     */
    private Integer entityId;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 实体应用别名
     */
    private String entityAppName;

    /**
     * 匹配上的值
     */
    private String value;

    public MatchedEntityParam() {  }

    public MatchedEntityParam(Integer ruleId, Integer entityType, Integer entityId, String entityName, String entityAppName, String value) {
        this.ruleId = ruleId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityName = entityName;
        this.entityAppName = entityAppName;
        this.value = value;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityAppName() {
        return entityAppName;
    }

    public void setEntityAppName(String entityAppName) {
        this.entityAppName = entityAppName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
