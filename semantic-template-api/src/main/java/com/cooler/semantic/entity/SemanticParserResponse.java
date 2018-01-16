package com.cooler.semantic.entity;

import java.io.Serializable;

public class SemanticParserResponse implements Serializable {
    //原始句子
    private String sentence;

    //响应类型
    private int responseType = 0;

    //计算得分
    private double score = 0d;

    //响应语句
    private String responseMsg;

    //状态
    private int state = 0;

    //积累的问话
    private int accumulateQueryCount;

    //响应的时间戳
    private long responseTimestamp;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
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
