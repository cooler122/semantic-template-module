package com.cooler.semantic.entity;

import java.util.Date;

public class WordCN {
    private Integer id;

    private String word;

    private Integer state;

    private Integer accountId;

    private Date createTime;

    public WordCN(Integer id, String word, Integer state, Integer accountId, Date createTime) {
        this.id = id;
        this.word = word;
        this.state = state;
        this.accountId = accountId;
        this.createTime = createTime;
    }

    public WordCN() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word == null ? null : word.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}