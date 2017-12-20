package com.cooler.semantic.component.biz;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.data.DataComponent;

public class FunctionBiz1 extends FunctionComponentBase {

    public FunctionBiz1(String id, int type, String inputDataBeanId, String outputDataBeanId) {
        super(id, type, inputDataBeanId, outputDataBeanId);
    }

    public String runIt(DataComponent inputDataComponent, DataComponent outputDataComponent) {
        System.out.println("so_1 running...");
        Object inputData = inputDataComponent.getData();
        System.out.println("so_1业务：处理Data1对象" + JSON.toJSONString(inputData) + " ，得到Data2对象，将Data2对象赋予outputDataBean中");
        String outputData = "so_1业务所得outputdata";
        outputDataComponent.setData(outputData);
        return "so_1_ok";
    }

}
