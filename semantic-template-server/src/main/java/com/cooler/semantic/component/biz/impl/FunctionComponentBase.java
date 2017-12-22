package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.biz.FunctionComponent;
import com.cooler.semantic.component.data.*;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.model.ContextOwner;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FunctionComponentBase<I, O> implements FunctionComponent {

    /**
     * 组件ID
     */
    protected String id;
    /**
     * 组件类型（逻辑组件、判断组件）
     */
    protected String processCode;
    /**
     * 输入数据组件
     */
    protected String inputDataBeanId;
    /**
     * 输出数据组件
     */
    protected String outputDataBeanId;
    /**
     * 3类数据的Map数据库（此类代表了本系统的核心方案，具体见applicationContext-components.xml的配置）
     */
    @Autowired
    private ComponentConstant componentConstant;
    /**
     * 此组件中的逻辑是否执行成功
     */
    protected boolean isSuccess = false;

    public FunctionComponentBase() {  }

    public FunctionComponentBase(String id, String processCode, String inputDataBeanId, String outputDataBeanId) {
        this.id = id;
        this.processCode = processCode;
        this.inputDataBeanId = inputDataBeanId;
        this.outputDataBeanId = outputDataBeanId;
    }

    /**
     * 此方法为组件的核心调配方法，能够让组件按照设定次序依次进行
     */
    public void run(ContextOwner contextOwner) {

        //1.获取输入参数体
        DataComponent<I> inputDataComponent = null;
        if(inputDataBeanId != null){
            inputDataComponent = componentConstant.getDataComponent(inputDataBeanId, contextOwner);                //获取输入 账户/用户/上下文 下的瞬时数据组件
        }

        //2.运行业务
        System.out.println("\n组件ID：" + this.id + "，流程编号：" + this.processCode + "，入参：" + JSON.toJSONString(inputDataComponent) + "，开始运行...");
        I inputBizData = inputDataComponent != null ? inputDataComponent.getData() : null;
        ComponentBizResult<O> componentBizResult = runBiz(inputBizData);                                                //运行子组件的逻辑，运行体中获得子组件的OutPutDataComponent

        //3.分析结果，保存数据
        String resultCode = componentBizResult.getResultCode();                                                         //返回执行结果码
        boolean isStore = componentBizResult.isStore();                                                                //返回输入输出类型
        O bizData = componentBizResult.getOutputData();                                                                 //返回输出数据体
        DataComponent outputDataComponent = bizData != null ? new DataComponentBase(outputDataBeanId, contextOwner, bizData.getClass().getSimpleName(), bizData) : new DataComponentBase(this.outputDataBeanId, contextOwner, null, null);
        if(isStore && outputDataBeanId != null && outputDataComponent != null){
            componentConstant.putDataComponent(outputDataComponent);                                                 //子组件的OutPutDataComponent保存到数据源ComponentConstant的Map中（后续最好用ThreadLocal实现此Map，放redis也行啊）
            System.out.println("输出参数：" + JSON.toJSONString(outputDataComponent));
        }

        if(resultCode.equals("END_S"))  return;                                                                      //流程出口（检测状态码，看是否结束）

        //4.带动下一个组件再来运行
        String nextComponentId = componentConstant.getFCIDByResultCode(resultCode);
        FunctionComponent nextFunctionComponent = componentConstant.getFunctionComponent(nextComponentId);
        System.out.println("转换 ：" + resultCode + "     --->    " + nextComponentId);

        if(nextFunctionComponent != null)  nextFunctionComponent.run(contextOwner);                                    //强迫停止，一般不会出现哪个组件为空的情况

    }

    /**
     * 本组件需要运行的业务逻辑，由本抽象类的具体子类来重写实现
     * @return  返回运行的结果体以及结果状态
     */
    protected ComponentBizResult<O> runBiz(I bizData) {
        return null;
    }





}
