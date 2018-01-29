package com.cooler.semantic.entity;

public class ReferRuleCondition {
    private Integer id;

    private Integer referRuleRelationId;

    private Integer entityId;

    private Integer logicExpression;

    private String entityParam;

    private Integer state;

    public ReferRuleCondition(Integer id, Integer referRuleRelationId, Integer entityId, Integer logicExpression, String entityParam, Integer state) {
        this.id = id;
        this.referRuleRelationId = referRuleRelationId;
        this.entityId = entityId;
        this.logicExpression = logicExpression;
        this.entityParam = entityParam;
        this.state = state;
    }

    public ReferRuleCondition() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReferRuleRelationId() {
        return referRuleRelationId;
    }

    public void setReferRuleRelationId(Integer referRuleRelationId) {
        this.referRuleRelationId = referRuleRelationId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getLogicExpression() {
        return logicExpression;
    }

    public void setLogicExpression(Integer logicExpression) {
        this.logicExpression = logicExpression;
    }

    public String getEntityParam() {
        return entityParam;
    }

    public void setEntityParam(String entityParam) {
        this.entityParam = entityParam == null ? null : entityParam.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}