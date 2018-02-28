package com.cooler.semantic.entity;

import java.util.Date;

public class LogDataProcess {
    private Integer id;

    private Date dateTime;

    private Integer accountId;

    private Integer userId;

    private Integer contextId;

    private String processTrace;

    private String detailContextOwner;

    private String sentence;

    private String sentenceModified;

    private String responseMsg;

    private Integer state;

    private Double score;

    private Integer responseType;

    private String responseTimestamp;

    private String configureParams;

    private String svIds;

    private String svWords;

    private String svWeights;

    private String lpmRuleScore;

    private String lpmMatchedRew;

    private String lpmMatchedRre;

    private String lpmLackedRre;

    private String cpmRuleScore;

    private String cpmMatchedRew;

    private String cpmMatchedRre;

    private String cpmLackedRre;

    private String fpmRuleScore;

    private String fpmMatchedRew;

    private String fpmMatchedRre;

    private String fpmLackedRre;

    private String selectResult;

    public LogDataProcess(Integer id, Date dateTime, Integer accountId, Integer userId, Integer contextId, String processTrace, String detailContextOwner, String sentence, String sentenceModified, String responseMsg, Integer state, Double score, Integer responseType, String responseTimestamp, String configureParams, String svIds, String svWords, String svWeights, String lpmRuleScore, String lpmMatchedRew, String lpmMatchedRre, String lpmLackedRre, String cpmRuleScore, String cpmMatchedRew, String cpmMatchedRre, String cpmLackedRre, String fpmRuleScore, String fpmMatchedRew, String fpmMatchedRre, String fpmLackedRre, String selectResult) {
        this.id = id;
        this.dateTime = dateTime;
        this.accountId = accountId;
        this.userId = userId;
        this.contextId = contextId;
        this.processTrace = processTrace;
        this.detailContextOwner = detailContextOwner;
        this.sentence = sentence;
        this.sentenceModified = sentenceModified;
        this.responseMsg = responseMsg;
        this.state = state;
        this.score = score;
        this.responseType = responseType;
        this.responseTimestamp = responseTimestamp;
        this.configureParams = configureParams;
        this.svIds = svIds;
        this.svWords = svWords;
        this.svWeights = svWeights;
        this.lpmRuleScore = lpmRuleScore;
        this.lpmMatchedRew = lpmMatchedRew;
        this.lpmMatchedRre = lpmMatchedRre;
        this.lpmLackedRre = lpmLackedRre;
        this.cpmRuleScore = cpmRuleScore;
        this.cpmMatchedRew = cpmMatchedRew;
        this.cpmMatchedRre = cpmMatchedRre;
        this.cpmLackedRre = cpmLackedRre;
        this.fpmRuleScore = fpmRuleScore;
        this.fpmMatchedRew = fpmMatchedRew;
        this.fpmMatchedRre = fpmMatchedRre;
        this.fpmLackedRre = fpmLackedRre;
        this.selectResult = selectResult;
    }

    public LogDataProcess() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public String getProcessTrace() {
        return processTrace;
    }

    public void setProcessTrace(String processTrace) {
        this.processTrace = processTrace == null ? null : processTrace.trim();
    }

    public String getDetailContextOwner() {
        return detailContextOwner;
    }

    public void setDetailContextOwner(String detailContextOwner) {
        this.detailContextOwner = detailContextOwner == null ? null : detailContextOwner.trim();
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence == null ? null : sentence.trim();
    }

    public String getSentenceModified() {
        return sentenceModified;
    }

    public void setSentenceModified(String sentenceModified) {
        this.sentenceModified = sentenceModified == null ? null : sentenceModified.trim();
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg == null ? null : responseMsg.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }

    public String getResponseTimestamp() {
        return responseTimestamp;
    }

    public void setResponseTimestamp(String responseTimestamp) {
        this.responseTimestamp = responseTimestamp == null ? null : responseTimestamp.trim();
    }

    public String getConfigureParams() {
        return configureParams;
    }

    public void setConfigureParams(String configureParams) {
        this.configureParams = configureParams == null ? null : configureParams.trim();
    }

    public String getSvIds() {
        return svIds;
    }

    public void setSvIds(String svIds) {
        this.svIds = svIds == null ? null : svIds.trim();
    }

    public String getSvWords() {
        return svWords;
    }

    public void setSvWords(String svWords) {
        this.svWords = svWords == null ? null : svWords.trim();
    }

    public String getSvWeights() {
        return svWeights;
    }

    public void setSvWeights(String svWeights) {
        this.svWeights = svWeights == null ? null : svWeights.trim();
    }

    public String getLpmRuleScore() {
        return lpmRuleScore;
    }

    public void setLpmRuleScore(String lpmRuleScore) {
        this.lpmRuleScore = lpmRuleScore == null ? null : lpmRuleScore.trim();
    }

    public String getLpmMatchedRew() {
        return lpmMatchedRew;
    }

    public void setLpmMatchedRew(String lpmMatchedRew) {
        this.lpmMatchedRew = lpmMatchedRew == null ? null : lpmMatchedRew.trim();
    }

    public String getLpmMatchedRre() {
        return lpmMatchedRre;
    }

    public void setLpmMatchedRre(String lpmMatchedRre) {
        this.lpmMatchedRre = lpmMatchedRre == null ? null : lpmMatchedRre.trim();
    }

    public String getLpmLackedRre() {
        return lpmLackedRre;
    }

    public void setLpmLackedRre(String lpmLackedRre) {
        this.lpmLackedRre = lpmLackedRre == null ? null : lpmLackedRre.trim();
    }

    public String getCpmRuleScore() {
        return cpmRuleScore;
    }

    public void setCpmRuleScore(String cpmRuleScore) {
        this.cpmRuleScore = cpmRuleScore == null ? null : cpmRuleScore.trim();
    }

    public String getCpmMatchedRew() {
        return cpmMatchedRew;
    }

    public void setCpmMatchedRew(String cpmMatchedRew) {
        this.cpmMatchedRew = cpmMatchedRew == null ? null : cpmMatchedRew.trim();
    }

    public String getCpmMatchedRre() {
        return cpmMatchedRre;
    }

    public void setCpmMatchedRre(String cpmMatchedRre) {
        this.cpmMatchedRre = cpmMatchedRre == null ? null : cpmMatchedRre.trim();
    }

    public String getCpmLackedRre() {
        return cpmLackedRre;
    }

    public void setCpmLackedRre(String cpmLackedRre) {
        this.cpmLackedRre = cpmLackedRre == null ? null : cpmLackedRre.trim();
    }

    public String getFpmRuleScore() {
        return fpmRuleScore;
    }

    public void setFpmRuleScore(String fpmRuleScore) {
        this.fpmRuleScore = fpmRuleScore == null ? null : fpmRuleScore.trim();
    }

    public String getFpmMatchedRew() {
        return fpmMatchedRew;
    }

    public void setFpmMatchedRew(String fpmMatchedRew) {
        this.fpmMatchedRew = fpmMatchedRew == null ? null : fpmMatchedRew.trim();
    }

    public String getFpmMatchedRre() {
        return fpmMatchedRre;
    }

    public void setFpmMatchedRre(String fpmMatchedRre) {
        this.fpmMatchedRre = fpmMatchedRre == null ? null : fpmMatchedRre.trim();
    }

    public String getFpmLackedRre() {
        return fpmLackedRre;
    }

    public void setFpmLackedRre(String fpmLackedRre) {
        this.fpmLackedRre = fpmLackedRre == null ? null : fpmLackedRre.trim();
    }

    public String getSelectResult() {
        return selectResult;
    }

    public void setSelectResult(String selectResult) {
        this.selectResult = selectResult == null ? null : selectResult.trim();
    }
}