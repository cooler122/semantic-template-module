package com.cooler.semantic.entity;

import java.util.Date;

public class REntityWord {
    private Integer id;

    private Integer entityId;

    private String entityName;

    private Integer wordId;

    private String word;

    private String normalWord;

    private Integer state;

    private Integer accountId;

    private String rMsg;

    private Date createTime;

    public REntityWord(Integer id, Integer entityId, String entityName, Integer wordId, String word, String normalWord, Integer state, Integer accountId, String rMsg, Date createTime) {
        this.id = id;
        this.entityId = entityId;
        this.entityName = entityName;
        this.wordId = wordId;
        this.word = word;
        this.normalWord = normalWord;
        this.state = state;
        this.accountId = accountId;
        this.rMsg = rMsg;
        this.createTime = createTime;
    }

    public REntityWord() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName == null ? null : entityName.trim();
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word == null ? null : word.trim();
    }

    public String getNormalWord() {
        return normalWord;
    }

    public void setNormalWord(String normalWord) {
        this.normalWord = normalWord == null ? null : normalWord.trim();
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

    public String getrMsg() {
        return rMsg;
    }

    public void setrMsg(String rMsg) {
        this.rMsg = rMsg == null ? null : rMsg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}