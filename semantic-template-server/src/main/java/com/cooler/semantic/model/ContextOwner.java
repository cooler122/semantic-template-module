package com.cooler.semantic.model;

public class ContextOwner {

    private Integer accountId;

    private Integer userId;

    private Integer contextId;

    private String ownerIndex;

    public ContextOwner(Integer accountId, Integer userId, Integer contextId) {
        this.accountId = accountId;
        this.userId = userId;
        this.contextId = contextId;
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

    public Integer getContextId() {
        return contextId;
    }

    public void setContextId(Integer contextId) {
        this.contextId = contextId;
    }

    public String getOwnerIndex() {
        return accountId + "_" + userId + "_" + contextId;
    }

    public void setOwnerIndex(String ownerIndex) {
        this.ownerIndex = accountId + "_" + userId + "_" + contextId;
    }
}
