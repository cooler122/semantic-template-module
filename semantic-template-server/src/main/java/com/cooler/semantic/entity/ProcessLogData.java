package com.cooler.semantic.entity;

public class ProcessLogData {
    private Integer id;

    private String processTrace;

    private String ownerIndex;

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

    private String lpmRewEntitys;

    private String lpmRewWords;

    private String lpmRewWeights;

    private String cpmRewEntitys;

    private String cpmRewWords;

    private String cpmRewWeights;

    private String fpmRewEntitys;

    private String fpmRewWords;

    private String fpmRewWeights;

    private String optimalRewEntitys;

    private String optimalRewWords;

    private String optimalRewWeights;

    public ProcessLogData(Integer id, String processTrace, String ownerIndex, String detailContextOwner, String sentence, String sentenceModified, String responseMsg, Integer state, Double score, Integer responseType, String responseTimestamp, String configureParams, String svIds, String svWords, String svWeights, String lpmRewEntitys, String lpmRewWords, String lpmRewWeights, String cpmRewEntitys, String cpmRewWords, String cpmRewWeights, String fpmRewEntitys, String fpmRewWords, String fpmRewWeights, String optimalRewEntitys, String optimalRewWords, String optimalRewWeights) {
        this.id = id;
        this.processTrace = processTrace;
        this.ownerIndex = ownerIndex;
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
        this.lpmRewEntitys = lpmRewEntitys;
        this.lpmRewWords = lpmRewWords;
        this.lpmRewWeights = lpmRewWeights;
        this.cpmRewEntitys = cpmRewEntitys;
        this.cpmRewWords = cpmRewWords;
        this.cpmRewWeights = cpmRewWeights;
        this.fpmRewEntitys = fpmRewEntitys;
        this.fpmRewWords = fpmRewWords;
        this.fpmRewWeights = fpmRewWeights;
        this.optimalRewEntitys = optimalRewEntitys;
        this.optimalRewWords = optimalRewWords;
        this.optimalRewWeights = optimalRewWeights;
    }

    public ProcessLogData() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessTrace() {
        return processTrace;
    }

    public void setProcessTrace(String processTrace) {
        this.processTrace = processTrace == null ? null : processTrace.trim();
    }

    public String getOwnerIndex() {
        return ownerIndex;
    }

    public void setOwnerIndex(String ownerIndex) {
        this.ownerIndex = ownerIndex == null ? null : ownerIndex.trim();
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

    public String getLpmRewEntitys() {
        return lpmRewEntitys;
    }

    public void setLpmRewEntitys(String lpmRewEntitys) {
        this.lpmRewEntitys = lpmRewEntitys == null ? null : lpmRewEntitys.trim();
    }

    public String getLpmRewWords() {
        return lpmRewWords;
    }

    public void setLpmRewWords(String lpmRewWords) {
        this.lpmRewWords = lpmRewWords == null ? null : lpmRewWords.trim();
    }

    public String getLpmRewWeights() {
        return lpmRewWeights;
    }

    public void setLpmRewWeights(String lpmRewWeights) {
        this.lpmRewWeights = lpmRewWeights == null ? null : lpmRewWeights.trim();
    }

    public String getCpmRewEntitys() {
        return cpmRewEntitys;
    }

    public void setCpmRewEntitys(String cpmRewEntitys) {
        this.cpmRewEntitys = cpmRewEntitys == null ? null : cpmRewEntitys.trim();
    }

    public String getCpmRewWords() {
        return cpmRewWords;
    }

    public void setCpmRewWords(String cpmRewWords) {
        this.cpmRewWords = cpmRewWords == null ? null : cpmRewWords.trim();
    }

    public String getCpmRewWeights() {
        return cpmRewWeights;
    }

    public void setCpmRewWeights(String cpmRewWeights) {
        this.cpmRewWeights = cpmRewWeights == null ? null : cpmRewWeights.trim();
    }

    public String getFpmRewEntitys() {
        return fpmRewEntitys;
    }

    public void setFpmRewEntitys(String fpmRewEntitys) {
        this.fpmRewEntitys = fpmRewEntitys == null ? null : fpmRewEntitys.trim();
    }

    public String getFpmRewWords() {
        return fpmRewWords;
    }

    public void setFpmRewWords(String fpmRewWords) {
        this.fpmRewWords = fpmRewWords == null ? null : fpmRewWords.trim();
    }

    public String getFpmRewWeights() {
        return fpmRewWeights;
    }

    public void setFpmRewWeights(String fpmRewWeights) {
        this.fpmRewWeights = fpmRewWeights == null ? null : fpmRewWeights.trim();
    }

    public String getOptimalRewEntitys() {
        return optimalRewEntitys;
    }

    public void setOptimalRewEntitys(String optimalRewEntitys) {
        this.optimalRewEntitys = optimalRewEntitys == null ? null : optimalRewEntitys.trim();
    }

    public String getOptimalRewWords() {
        return optimalRewWords;
    }

    public void setOptimalRewWords(String optimalRewWords) {
        this.optimalRewWords = optimalRewWords == null ? null : optimalRewWords.trim();
    }

    public String getOptimalRewWeights() {
        return optimalRewWeights;
    }

    public void setOptimalRewWeights(String optimalRewWeights) {
        this.optimalRewWeights = optimalRewWeights == null ? null : optimalRewWeights.trim();
    }
}