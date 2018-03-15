package com.cooler.semantic.entity;

public class Account {
    private Integer id;

    private String accountName;

    private String accountOwner;

    private Integer userCount;

    private String passwordMd5;

    private Integer state;

    public Account(Integer id, String accountName, String accountOwner, Integer userCount, String passwordMd5, Integer state) {
        this.id = id;
        this.accountName = accountName;
        this.accountOwner = accountOwner;
        this.userCount = userCount;
        this.passwordMd5 = passwordMd5;
        this.state = state;
    }

    public Account() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner == null ? null : accountOwner.trim();
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5 == null ? null : passwordMd5.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}