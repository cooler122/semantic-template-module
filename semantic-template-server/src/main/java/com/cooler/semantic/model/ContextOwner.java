package com.cooler.semantic.model;

import java.io.Serializable;

public class ContextOwner implements Serializable{

    private Integer accountId;

    private Integer userId;

    private Integer contextId;

    private String ownerIndex;

    private String last1OwnerIndex;

    private String last2OwnerIndex;

    private String last3OwnerIndex;

    private String last4OwnerIndex;

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

    public String getLast1OwnerIndex() {
        return accountId + "_" + userId + "_" + (contextId - 1);
    }

    public void setLast1OwnerIndex(String last1OwnerIndex) {
        this.last1OwnerIndex = accountId + "_" + userId + "_" + (contextId - 1);
    }

    public String getLast2OwnerIndex() {
        return accountId + "_" + userId + "_" + (contextId - 2);
    }

    public void setLast2OwnerIndex(String last2OwnerIndex) {
        this.last2OwnerIndex = accountId + "_" + userId + "_" + (contextId - 2);
    }

    public String getLast3OwnerIndex() {
        return accountId + "_" + userId + "_" + (contextId - 3);
    }

    public void setLast3OwnerIndex(String last3OwnerIndex) {
        this.last3OwnerIndex = accountId + "_" + userId + "_" + (contextId - 3);
    }

    public String getLast4OwnerIndex() {
        return accountId + "_" + userId + "_" + (contextId - 4);
    }

    public void setLast4OwnerIndex(String last4OwnerIndex) {
        this.last4OwnerIndex = accountId + "_" + userId + "_" + (contextId - 4);
    }

    public String getLastNOwnerIndex(int n){
        if(n > 5){                                                                                                      //规定最多不能回溯到前面5轮对话
            n = 5;
        }
        return accountId + "_" + userId + "_" + (contextId - n);
    }

    /**
     * 获取上n轮对话的contextId
     * @param n
     * @return
     */
    public Integer getLastNContextId(int n){
        if(n > 5){                                                                                                      //规定最多不能回溯到前面5轮对话
            n = 5;
        }
        return contextId - n;
    }
}
