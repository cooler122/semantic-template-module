package com.cooler.semantic.entity;

import java.io.Serializable;
import java.util.List;

public class SemanticParserResponse implements Serializable {
    /**
     * 账户集
     */
    private List<Integer> accountIds;

    /**
     * 用户号
     */
    private Integer userId;

    /**
     * 上下文编号
     */
    private Integer contextId;

    //原始句子
    private String sentence;

    //修改后的句子
    private String sentenceModified;

    //响应类型
    private int responseType = 0;

    //计算得分
    private double score = 0d;

    //响应语句
    private String responseMsg;

    //状态
    private int state = 0;

    //对某一领域（规则、实体）积累的问话
    private int accumulateQueryCount;

    //响应的时间戳
    private long responseTimestamp;

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

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceModified() {
        return sentenceModified;
    }

    public void setSentenceModified(String sentenceModified) {
        this.sentenceModified = sentenceModified;
    }

    public int getResponseType() {
        return responseType;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAccumulateQueryCount() {
        return accumulateQueryCount;
    }

    public void setAccumulateQueryCount(int accumulateQueryCount) {
        this.accumulateQueryCount = accumulateQueryCount;
    }

    public long getResponseTimestamp() {
        return responseTimestamp;
    }

    public void setResponseTimestamp(long responseTimestamp) {
        this.responseTimestamp = responseTimestamp;
    }
}
