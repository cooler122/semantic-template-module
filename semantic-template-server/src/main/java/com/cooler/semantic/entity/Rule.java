package com.cooler.semantic.entity;

import java.util.Date;

public class Rule {
    private Integer id;

    private String ruleName;

    private Integer intentId;

    private Integer senceId;

    private Integer accountId;

    private String originalSentence;

    private String ruleTemplate;

    private String ruleMsg;

    private Integer referRuleId;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    public Rule(Integer id, String ruleName, Integer intentId, Integer senceId, Integer accountId, String originalSentence, String ruleTemplate, String ruleMsg, Integer referRuleId, Integer state, Date createTime, Date updateTime) {
        this.id = id;
        this.ruleName = ruleName;
        this.intentId = intentId;
        this.senceId = senceId;
        this.accountId = accountId;
        this.originalSentence = originalSentence;
        this.ruleTemplate = ruleTemplate;
        this.ruleMsg = ruleMsg;
        this.referRuleId = referRuleId;
        this.state = state;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Rule() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public Integer getIntentId() {
        return intentId;
    }

    public void setIntentId(Integer intentId) {
        this.intentId = intentId;
    }

    public Integer getSenceId() {
        return senceId;
    }

    public void setSenceId(Integer senceId) {
        this.senceId = senceId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getOriginalSentence() {
        return originalSentence;
    }

    public void setOriginalSentence(String originalSentence) {
        this.originalSentence = originalSentence == null ? null : originalSentence.trim();
    }

    public String getRuleTemplate() {
        return ruleTemplate;
    }

    public void setRuleTemplate(String ruleTemplate) {
        this.ruleTemplate = ruleTemplate == null ? null : ruleTemplate.trim();
    }

    public String getRuleMsg() {
        return ruleMsg;
    }

    public void setRuleMsg(String ruleMsg) {
        this.ruleMsg = ruleMsg == null ? null : ruleMsg.trim();
    }

    public Integer getReferRuleId() {
        return referRuleId;
    }

    public void setReferRuleId(Integer referRuleId) {
        this.referRuleId = referRuleId;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}