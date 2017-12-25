package com.cooler.semantic.entity;

public class AccountConfiguration {
    private Integer id;

    private Integer accountId;

    private Integer userId;

    private Boolean canBreakContext;

    private Boolean canBatchQuery;

    private Integer ruleMaxQueryCount;

    private Integer entityMaxQueryCount;

    private Integer contextWaitTime;

    private Double accuracyThreshold;

    private Integer logType;

    private Integer algorithmType;

    public AccountConfiguration(Integer id, Integer accountId, Integer userId, Boolean canBreakContext, Boolean canBatchQuery, Integer ruleMaxQueryCount, Integer entityMaxQueryCount, Integer contextWaitTime, Double accuracyThreshold, Integer logType, Integer algorithmType) {
        this.id = id;
        this.accountId = accountId;
        this.userId = userId;
        this.canBreakContext = canBreakContext;
        this.canBatchQuery = canBatchQuery;
        this.ruleMaxQueryCount = ruleMaxQueryCount;
        this.entityMaxQueryCount = entityMaxQueryCount;
        this.contextWaitTime = contextWaitTime;
        this.accuracyThreshold = accuracyThreshold;
        this.logType = logType;
        this.algorithmType = algorithmType;
    }

    public AccountConfiguration() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getCanBreakContext() {
        return canBreakContext;
    }

    public void setCanBreakContext(Boolean canBreakContext) {
        this.canBreakContext = canBreakContext;
    }

    public Boolean getCanBatchQuery() {
        return canBatchQuery;
    }

    public void setCanBatchQuery(Boolean canBatchQuery) {
        this.canBatchQuery = canBatchQuery;
    }

    public Integer getRuleMaxQueryCount() {
        return ruleMaxQueryCount;
    }

    public void setRuleMaxQueryCount(Integer ruleMaxQueryCount) {
        this.ruleMaxQueryCount = ruleMaxQueryCount;
    }

    public Integer getEntityMaxQueryCount() {
        return entityMaxQueryCount;
    }

    public void setEntityMaxQueryCount(Integer entityMaxQueryCount) {
        this.entityMaxQueryCount = entityMaxQueryCount;
    }

    public Integer getContextWaitTime() {
        return contextWaitTime;
    }

    public void setContextWaitTime(Integer contextWaitTime) {
        this.contextWaitTime = contextWaitTime;
    }

    public Double getAccuracyThreshold() {
        return accuracyThreshold;
    }

    public void setAccuracyThreshold(Double accuracyThreshold) {
        this.accuracyThreshold = accuracyThreshold;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public Integer getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(Integer algorithmType) {
        this.algorithmType = algorithmType;
    }
}