package com.cooler.semantic.component;

public class ComponentBizResult<O> {
    /**
     * 结果返回码
     */
    private String resultCode = null;

    /**
     * 数据保存码(1，保存到本地；2，保存到远程)
     */
    private int saveCode = 1;
    /**
     * 输出参数数据组件
     */
    private O outputData;
    /**
     * 错误码
     */
    private String errorCode = null;

    /**
     * 执行通过了，返回下面3个码
     * @param resultCode    结果码
     */
    public ComponentBizResult(String resultCode) {
        this.resultCode = resultCode;
        this.saveCode = 0;
    }

    /**
     * 执行通过了，返回下面3个码
     * @param resultCode    结果码
     * @param saveCode  保存状态码
     * @param outputData    数据体
     */
    public ComponentBizResult(String resultCode,int saveCode, O outputData) {
        this.resultCode = resultCode;
        this.saveCode = saveCode;
        this.outputData = outputData;
    }

    /**
     * 发生错误了，只返回resultCode和errorCode
     * @param resultCode    结果码
     * @param errorCode     错误原因码
     */
    public ComponentBizResult(String resultCode, String errorCode) {
        this.resultCode = resultCode;
        this.errorCode = errorCode;
    }

    public int getSaveCode() {
        return saveCode;
    }

    public void setSaveCode(int saveCode) {
        this.saveCode = saveCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
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
