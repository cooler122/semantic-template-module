package com.cooler.semantic.component.biz;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.data.DataBean2;
import com.cooler.semantic.component.data.DataBean3;
import com.cooler.semantic.component.data.DataComponent;

public class FunctionBiz2 extends FunctionComponentBase {

    public FunctionBiz2(String id, int type, String inputDataBeanId, String outputDataBeanId) {
        super(id, type, inputDataBeanId, outputDataBeanId);
    }

    public String runIt(DataComponent inputDataComponent, DataComponent outputDataComponent) {
        System.out.println("so_2 running...");

        Object inputData = inputDataComponent.getData();

        System.out.println("so_2业务：处理Data2对象" + JSON.toJSONString(inputData) + " ，得到Data3对象，将Data3对象赋予outputDataBean中");
        DataBean3 outputData = new DataBean3(3, "databean3", "so_2业务所得outputdata");

        outputDataComponent.setData(outputData);

        if(ComponentConstant.loopCount >= 5){    //此处可以根据实际情况来判断返回何种信息给外界，这里只需要返回自己的状态即可，不用耦合外界关联，关联交给关联的Map
            return "so_2_ok";
        }
        return "so_2_no";
    }

}
