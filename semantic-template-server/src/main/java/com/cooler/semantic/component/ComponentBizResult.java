package com.cooler.semantic.component;

import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.IOType;

public class ComponentBizResult {
    /**
     * 结果返回码
     */
    private String resultCode = null;
    /**
     * 组件输入输出类型
     */
    private IOType ioType = IOType.NO_IN_OUT;
    /**
     * 输出参数数据组件
     */
    private DataComponent outputDataComponent;

    public ComponentBizResult(String resultCode, IOType ioType, DataComponent outputDataComponent) {
        this.resultCode = resultCode;
        this.ioType = ioType;
        this.outputDataComponent = outputDataComponent;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public IOType getIoType() {
        return ioType;
    }

    public void setIoType(IOType ioType) {
        this.ioType = ioType;
    }

    public DataComponent getOutputDataComponent() {
        return outputDataComponent;
    }

    public void setOutputDataComponent(DataComponent outputDataComponent) {
        this.outputDataComponent = outputDataComponent;
    }
}
