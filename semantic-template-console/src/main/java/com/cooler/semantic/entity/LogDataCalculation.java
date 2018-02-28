package com.cooler.semantic.entity;

import java.util.Date;

public class LogDataCalculation {
    private Integer id;

    private Date dateTime;

    private Integer accountId;

    private Integer userId;

    private Integer contextId;

    private String processTrace;

    private String detailContextOwner;

    private String cpmJsonData;

    private String lpmJsonData;

    private String fpmJsonData;

    public LogDataCalculation(Integer id, Date dateTime, Integer accountId, Integer userId, Integer contextId, String processTrace, String detailContextOwner, String cpmJsonData, String lpmJsonData, String fpmJsonData) {
        this.id = id;
        this.dateTime = dateTime;
        this.accountId = accountId;
        this.userId = userId;
        this.contextId = contextId;
        this.processTrace = processTrace;
        this.detailContextOwner = detailContextOwner;
        this.cpmJsonData = cpmJsonData;
        this.lpmJsonData = lpmJsonData;
        this.fpmJsonData = fpmJsonData;
    }

    public LogDataCalculation() {
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

    public String getCpmJsonData() {
        return cpmJsonData;
    }

    public void setCpmJsonData(String cpmJsonData) {
        this.cpmJsonData = cpmJsonData;
    }

    public String getLpmJsonData() {
        return lpmJsonData;
    }

    public void setLpmJsonData(String lpmJsonData) {
        this.lpmJsonData = lpmJsonData;
    }

    public String getFpmJsonData() {
        return fpmJsonData;
    }

    public void setFpmJsonData(String fpmJsonData) {
        this.fpmJsonData = fpmJsonData;
    }
}