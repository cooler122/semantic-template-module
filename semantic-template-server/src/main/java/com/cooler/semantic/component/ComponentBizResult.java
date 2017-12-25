package com.cooler.semantic.component;

public class ComponentBizResult<O> {
    /**
     * 结果返回码
     */
    private String resultCode = null;
    /**
     * 数据体是否保存（默认不保存，保存则要手动设置true）
     */
    private boolean isStore = false;
    /**
     * 输出参数数据组件
     */
    private O outputData;
    /**
     * 错误码
     */
    private String errorCode = null;

    public ComponentBizResult(String resultCode, boolean isStore, O outputData) {
        this.resultCode = resultCode;
        this.isStore = isStore;
        this.outputData = outputData;
    }

    public ComponentBizResult(String resultCode, String errorCode) {
        this.resultCode = resultCode;
        this.errorCode = errorCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isStore() {
        return isStore;
    }

    public void setStore(boolean store) {
        isStore = store;
    }

    public O getOutputData() {
        return outputData;
    }

    public void setOutputData(O outputData) {
        this.outputData = outputData;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
