package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.biz.FunctionComponent;
import com.cooler.semantic.component.data.*;
import com.cooler.semantic.constant.IOType;
import com.cooler.semantic.component.ComponentBizResult;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

public abstract class FunctionComponentBase<I> implements FunctionComponent {

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
    public void run() {
        //0.准备好最新数据
        Map<String, DataComponent> dataBeanMap =  componentConstant.getDataBeanMap();                       //用户流程数据，大量变化（后面最好放到redis里面）
        Map<String, FunctionComponent> functionComponentMap = componentConstant.getFunctionComponentMap();  //功能组件，在此系统中恒定不变
        Map<String, String> nextComponentIdMap = componentConstant.getNextComponentIdMap();                 //流程关系，在此系统中恒定不变

        //1.获取输入参数体

        String inputDataBeanId = this.inputDataBeanId;                                  //查找输入参数体ID
        DataComponent inputDataComponent = dataBeanMap.get(inputDataBeanId);               //获取输入参数体
        String outputDataBeanId = this.outputDataBeanId;

        //2.运行业务
        System.out.println();
        System.out.println("组件ID：" + this.id + "，流程编号：" + this.processCode + "，入参：" + JSON.toJSONString(inputDataComponent) + "，开始运行...");

        ComponentBizResult componentBizResult = runBiz(inputDataComponent);                //运行子组件的逻辑，运行体中获得子组件的OutPutDataComponent

        String resultCode = componentBizResult.getResultCode();                             //返回执行结果码
        IOType ioType = componentBizResult.getIoType();                                     //返回输入输出类型
        DataComponent outputDataComponent = componentBizResult.getOutputDataComponent();    //返回输出数据体
        if(ioType != IOType.IN && ioType != IOType.NO_IN_OUT && outputDataBeanId != null && outputDataComponent != null){
            dataBeanMap.put(outputDataBeanId, outputDataComponent);                         //子组件的OutPutDataComponent保存到数据源ComponentConstant的Map中（后续最好用ThreadLocal实现此Map，放redis也行啊）
        }
        System.out.println("输出参数：" + JSON.toJSONString(outputDataComponent));

        if(resultCode.equals("END_S"))
            return;                                                                         //流程出口（检测状态码，看是否结束）

        //3.带动下一个组件再来运行
        String nextComponentId = nextComponentIdMap.get(resultCode);
        FunctionComponent nextFunctionComponent = functionComponentMap.get(nextComponentId);
        System.out.println("转换 ：" + resultCode + "     --->    " + nextComponentId);

        ComponentConstant.loopCount ++;

        if(nextFunctionComponent != null)                                                   //强迫停止，一般不会出现哪个组件为空的情况
            nextFunctionComponent.run();
    }

    /**
     * 本组件需要运行的业务逻辑，由本抽象类的具体子类来重写实现(有输入，有输出）
     * @return  下一个运行组件的组件ID
     */
    protected ComponentBizResult runBiz(DataComponent<I> inputDataComponent) {
        return null;
    }



}
