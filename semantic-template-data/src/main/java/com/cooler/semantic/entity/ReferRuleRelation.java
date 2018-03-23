package com.cooler.semantic.entity;

public class ReferRuleRelation {
    private Integer id;

    private Integer ruleId;

    private String ruleName;

    private Integer referRuleId;

    private String referRuleName;

    private Integer accountId;

    private Integer guideIntentId;

    private Byte hasConditions;

    private Integer state;

    public ReferRuleRelation(Integer id, Integer ruleId, String ruleName, Integer referRuleId, String referRuleName, Integer accountId, Integer guideIntentId, Byte hasConditions, Integer state) {
        this.id = id;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.referRuleId = referRuleId;
        this.referRuleName = referRuleName;
        this.accountId = accountId;
        this.guideIntentId = guideIntentId;
        this.hasConditions = hasConditions;
        this.state = state;
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
        this.ruleName = ruleName;
    }

    public Integer getReferRuleId() {
        return referRuleId;
    }

    public void setReferRuleId(Integer referRuleId) {
        this.referRuleId = referRuleId;
    }

    public String getReferRuleName() {
        return referRuleName;
    }

    public void setReferRuleName(String referRuleName) {
        this.referRuleName = referRuleName;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getGuideIntentId() {
        return guideIntentId;
    }

    public void setGuideIntentId(Integer guideIntentId) {
        this.guideIntentId = guideIntentId;
    }

    public Byte getHasConditions() {
        return hasConditions;
    }

    public void setHasConditions(Byte hasConditions) {
        this.hasConditions = hasConditions;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}