package com.cooler.semantic.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ContextOwner implements Serializable{

    private Integer coreAccountId;

    private List<Integer> accountIds;

    private Integer userId;

    private Integer contextId;

    private String ownerIndex;

    private String last1OwnerIndex;

    private String last2OwnerIndex;

    private String last3OwnerIndex;

    private String last4OwnerIndex;

    public ContextOwner(List<Integer> accountIds, Integer userId, Integer contextId) {
        this.coreAccountId = accountIds.get(0);
        this.accountIds = accountIds;
        this.userId = userId;
        this.contextId = contextId;
    }

    public Integer getCoreAccountId() {
        return accountIds.get(0);
    }

    public void setCoreAccountId(Integer coreAccountId) {
        this.coreAccountId = coreAccountId;
    }

    public List<Integer> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<Integer> accountIds) {
        this.accountIds = accountIds;
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
        return coreAccountId + "_" + userId + "_" + contextId;
    }

    public void setOwnerIndex(String ownerIndex) {
        this.ownerIndex = coreAccountId + "_" + userId + "_" + contextId;
    }

    public String getLast1OwnerIndex() {
        return coreAccountId + "_" + userId + "_" + (contextId - 1);
    }

    public void setLast1OwnerIndex(String last1OwnerIndex) {
        this.last1OwnerIndex = coreAccountId + "_" + userId + "_" + (contextId - 1);
    }

    public String getLast2OwnerIndex() {
        return coreAccountId + "_" + userId + "_" + (contextId - 2);
    }

    public void setLast2OwnerIndex(String last2OwnerIndex) {
        this.last2OwnerIndex = coreAccountId + "_" + userId + "_" + (contextId - 2);
    }

    public String getLast3OwnerIndex() {
        return coreAccountId + "_" + userId + "_" + (contextId - 3);
    }

    public void setLast3OwnerIndex(String last3OwnerIndex) {
        this.last3OwnerIndex = coreAccountId + "_" + userId + "_" + (contextId - 3);
    }

    public String getLast4OwnerIndex() {
        return coreAccountId + "_" + userId + "_" + (contextId - 4);
    }

    public void setLast4OwnerIndex(String last4OwnerIndex) {
        this.last4OwnerIndex = coreAccountId + "_" + userId + "_" + (contextId - 4);
    }

    public String getDetailContextOwner(){
        return Arrays.toString(this.getAccountIds().toArray()) + "_" + this.getUserId() + "_" + this.getContextId();
    }

    public String getLastNOwnerIndex(int n){
        if(n > 5){                                                                                                      //规定最多不能回溯到前面5轮对话
            n = 5;
        }
        return coreAccountId + "_" + userId + "_" + (contextId - n);
    }

    public String getLastNData(int n, String dataName){
        if(n > 5) n = 5;
        return coreAccountId + "_" + userId + "_" + (contextId - n) + "_" + dataName;
    }

    /**
     * 获取上n轮对话的contextId
     * @param n
     * @return
     */
    public Integer getLastNContextId(int n){
        if(n > 5) n = 5;
        return contextId - n;
    }
}
