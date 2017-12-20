package com.cooler.semantic.component.biz;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.data.*;

public abstract class FunctionComponentBase implements FunctionComponent {

    /**
     * 组件ID
     */
    protected String id;
    /**
     * 组件类型（逻辑组件、判断组件）
     */
    protected int type;
    /**
     * 输入数据组件
     */
    protected String inputDataBeanId;
    /**
     * 输出数据组件
     */
    protected String outputDataBeanId;

    /**
     * 此组件中的逻辑是否执行成功
     */
    protected boolean isSuccess = false;

    public FunctionComponentBase() {  }

    public FunctionComponentBase(String id, int type, String inputDataBeanId, String outputDataBeanId) {
        this.id = id;
        this.type = type;
        this.inputDataBeanId = inputDataBeanId;
        this.outputDataBeanId = outputDataBeanId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInputDataBeanId() {
        return inputDataBeanId;
    }

    public void setInputDataBeanId(String inputDataBeanId) {
        this.inputDataBeanId = inputDataBeanId;
    }

    public String getOutputDataBeanId() {
        return outputDataBeanId;
    }

    public void setOutputDataBeanId(String outputDataBeanId) {
        this.outputDataBeanId = outputDataBeanId;
    }

    public void doit() {
        //1.获取输入参数体
        String inputDataBeanId = this.getInputDataBeanId();                     //查找输入参数体ID
        String outputDataBeanId = this.getOutputDataBeanId();
        DataComponent inputDataComponent = ComponentConstant.dataBeanMap.get(inputDataBeanId);              //获取输入参数体
        DataComponent outputDataComponent = ComponentConstant.dataBeanMap.get(outputDataBeanId);            //有可能是NULL值

        //2.运行业务
        System.out.println();
        System.out.println("组件ID：" + this.getId() + "，组件类型：" + this.getType() + "，入参：" + JSON.toJSONString(inputDataComponent) + "，开始运行...");
        //运行本组件的逻辑，获得本对象的OutPutDataComponent，然后保存到一个Map中（此处最好用ThreadLocal实现此Map，放redis也行啊）
        String selectNextId = runIt(inputDataComponent, outputDataComponent);
        ComponentConstant.dataBeanMap.put(outputDataBeanId, outputDataComponent);

        if(selectNextId == null) return;                                      //出口

        //3.带动下一个组件再来运行
        String nextComponentId = ComponentConstant.nextComponentIdMap.get(selectNextId);
        FunctionComponent nextFunctionComponent = ComponentConstant.functionComponentMap.get(nextComponentId);
        System.out.println(selectNextId + " --->    " + nextComponentId);

        ComponentConstant.loopCount ++;

        nextFunctionComponent.doit();

    }

    /**
     * 本组件需要运行的业务逻辑
     * @return  下一个运行组件的组件ID
     */

    protected String runIt(DataComponent inputDataComponent, DataComponent outputDataComponent) {
        return null;
    }


}
