package com.cooler.semantic.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogDataProcessDTO {
    private Integer id;

    private Date dateTime;

    private Integer contextId;

    private String sentence;

    private String sentenceModified;

    private String responseMsg;

    private Integer state;

    private Integer responseType;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public Integer getId() {
        return id;
    }

    public LogDataProcessDTO(Integer id, Date dateTime, Integer contextId, String sentence, String sentenceModified, String responseMsg, Integer state, Integer responseType) {
        this.id = id;
        this.dateTime = dateTime;
        this.contextId = contextId;
        this.sentence = sentence;
        this.sentenceModified = sentenceModified;
        this.responseMsg = responseMsg;
        this.state = state;
        this.responseType = responseType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateTime() {
        String formatedDate = dateFormat.format(dateTime);
        return formatedDate;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }
}
