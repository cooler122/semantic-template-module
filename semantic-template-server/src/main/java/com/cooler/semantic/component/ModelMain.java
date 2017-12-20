package com.cooler.semantic.component;

import com.cooler.semantic.component.biz.FunctionBiz1;
import com.cooler.semantic.component.biz.FunctionBiz2;
import com.cooler.semantic.component.biz.FunctionBiz3;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.*;

public class ModelMain {
    public static void main(String args[]){
        //数据组件
        DataComponent data_1 = new DataComponentBase("data_1", "DataBean1", "abc");
        DataComponent data_2 = new DataComponentBase("data_2", "DataBean2", null);
        DataComponent data_3 = new DataComponentBase("data_3", "DataBean3", null);

        ComponentConstant.dataBeanMap.put("data_1", data_1);
        ComponentConstant.dataBeanMap.put("data_2", data_2);
        ComponentConstant.dataBeanMap.put("data_3", data_3);

        //逻辑组件
        FunctionComponentBase so_1 = new FunctionBiz1("so_1", 1, "data_1", "data_2");
        FunctionComponentBase so_2 = new FunctionBiz2("so_2", 1, "data_2", "data_3");
        FunctionComponentBase so_3 = new FunctionBiz3("so_3", 1, "data_3", null);

        ComponentConstant.functionComponentMap.put("so_1", so_1);
        ComponentConstant.functionComponentMap.put("so_2", so_2);
        ComponentConstant.functionComponentMap.put("so_3", so_3);

        //流程关联
        ComponentConstant.nextComponentIdMap.put("so_1_ok", "so_2");
        ComponentConstant.nextComponentIdMap.put("so_2_ok", "so_3");

        //第一个组件开始执行
        so_1.go();

    }

}
