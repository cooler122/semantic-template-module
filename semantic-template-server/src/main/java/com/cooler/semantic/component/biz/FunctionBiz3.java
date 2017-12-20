package com.cooler.semantic.component.biz;

import com.cooler.semantic.component.data.DataComponent;

public class FunctionBiz3 extends FunctionComponentBase {
    public FunctionBiz3(String id, int type, String inputDataBeanId, String outputDataBeanId) {
        super(id, type, inputDataBeanId, outputDataBeanId);
    }


    public String runIt(DataComponent inputDataComponent, DataComponent outputDataComponent) {
        System.out.println("so_3 running...");
        return null;
    }

}
