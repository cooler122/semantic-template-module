package com.cooler.semantic.entity;

public class WordNatureRate {
    private Integer id;

    private String wordNature;

    private Double rate;

    private Integer accountId;

    private Integer state;

    private String wordNatureMsg;

    public WordNatureRate(Integer id, String wordNature, Double rate, Integer accountId, Integer state, String wordNatureMsg) {
        this.id = id;
        this.wordNature = wordNature;
        this.rate = rate;
        this.accountId = accountId;
        this.state = state;
        this.wordNatureMsg = wordNatureMsg;
    }

    public WordNatureRate() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWordNature() {
        return wordNature;
    }

    public void setWordNature(String wordNature) {
        this.wordNature = wordNature == null ? null : wordNature.trim();
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getWordNatureMsg() {
        return wordNatureMsg;
    }

    public void setWordNatureMsg(String wordNatureMsg) {
        this.wordNatureMsg = wordNatureMsg == null ? null : wordNatureMsg.trim();
    }
}