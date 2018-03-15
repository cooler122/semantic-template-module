package com.cooler.semantic.entity;

import java.util.Date;

public class AnaphoraWord {
    private Integer id;

    private String anaphoraWord;

    private Integer anaphoraEntityId;

    private Integer state;

    private Integer accountId;

    private Date crateTime;

    public AnaphoraWord(Integer id, String anaphoraWord, Integer anaphoraEntityId, Integer state, Integer accountId, Date crateTime) {
        this.id = id;
        this.anaphoraWord = anaphoraWord;
        this.anaphoraEntityId = anaphoraEntityId;
        this.state = state;
        this.accountId = accountId;
        this.crateTime = crateTime;
    }

    public AnaphoraWord() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnaphoraWord() {
        return anaphoraWord;
    }

    public void setAnaphoraWord(String anaphoraWord) {
        this.anaphoraWord = anaphoraWord == null ? null : anaphoraWord.trim();
    }

    public Integer getAnaphoraEntityId() {
        return anaphoraEntityId;
    }

    public void setAnaphoraEntityId(Integer anaphoraEntityId) {
        this.anaphoraEntityId = anaphoraEntityId;
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

    public Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(Date crateTime) {
        this.crateTime = crateTime;
    }
}