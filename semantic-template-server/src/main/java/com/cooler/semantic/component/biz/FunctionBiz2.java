package com.cooler.semantic.component.biz;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.data.DataComponent;

public class FunctionBiz2 extends FunctionComponentBase {
    public FunctionBiz2(String id, int type, String inputDataBeanId, String outputDataBeanId) {
        super(id, type, inputDataBeanId, outputDataBeanId);
    }

    public String runIt(DataComponent inputDataComponent, DataComponent outputDataComponent) {
        System.out.println("so_2 running...");
        Object inputData = inputDataComponent.getData();
        System.out.println("so_2业务：处理Data2对象" + JSON.toJSONString(inputData) + " ，得到Data3对象，将Data3对象赋予outputDataBean中");
        String outputData = "so_2业务所得outputdata";
        outputDataComponent.setData(outputData);
        return "so_2_ok";
    }

}
