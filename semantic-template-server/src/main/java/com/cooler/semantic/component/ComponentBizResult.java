package com.cooler.semantic.component;

public class ComponentBizResult<O> {
    /**
     * 结果返回码
     */
    private String resultCode = null;
    /**
     * 数据体是否保存
     */
    private boolean isStore = false;
    /**
     * 输出参数数据组件
     */
    private O outputData;

    public ComponentBizResult(String resultCode, boolean isStore, O outputData) {
        this.resultCode = resultCode;
        this.isStore = isStore;
        this.outputData = outputData;
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
}
