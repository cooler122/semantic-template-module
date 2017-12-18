package com.cooler.semantic.entity;

public class Account {
    private Integer accountId;

    private String apiKey;

    private Boolean canBreakContext;

    private Integer entityMaxQueryCount;

    private Integer contextWaitTime;

    private Double accuracyThreshold;

    private Integer intentRuleIngateOutgate;

    private Integer intentRuleNonIngateOutgate;

    private Integer logType;

    private Integer algorithmType;

    public Account(Integer accountId, String apiKey, Boolean canBreakContext, Integer entityMaxQueryCount, Integer contextWaitTime, Double accuracyThreshold, Integer intentRuleIngateOutgate, Integer intentRuleNonIngateOutgate, Integer logType, Integer algorithmType) {
        this.accountId = accountId;
        this.apiKey = apiKey;
        this.canBreakContext = canBreakContext;
        this.entityMaxQueryCount = entityMaxQueryCount;
        this.contextWaitTime = contextWaitTime;
        this.accuracyThreshold = accuracyThreshold;
        this.intentRuleIngateOutgate = intentRuleIngateOutgate;
        this.intentRuleNonIngateOutgate = intentRuleNonIngateOutgate;
        this.logType = logType;
        this.algorithmType = algorithmType;
    }

    public Account() {
        super();
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey == null ? null : apiKey.trim();
    }

    public Boolean getCanBreakContext() {
        return canBreakContext;
    }

    public void setCanBreakContext(Boolean canBreakContext) {
        this.canBreakContext = canBreakContext;
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

    public Integer getIntentRuleIngateOutgate() {
        return intentRuleIngateOutgate;
    }

    public void setIntentRuleIngateOutgate(Integer intentRuleIngateOutgate) {
        this.intentRuleIngateOutgate = intentRuleIngateOutgate;
    }

    public Integer getIntentRuleNonIngateOutgate() {
        return intentRuleNonIngateOutgate;
    }

    public void setIntentRuleNonIngateOutgate(Integer intentRuleNonIngateOutgate) {
        this.intentRuleNonIngateOutgate = intentRuleNonIngateOutgate;
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