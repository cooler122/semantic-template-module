package com.cooler.semantic.component.biz;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.model.ContextOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component("verdictComponentBase")
public class VerdictComponentBase<I, O> implements SemanticComponent{
    /**
     * 组件ID
     */
    protected String id;

    /**
     * 组件类型（1功能组件、2判断组件）
     */
    protected int type = 2;
    /**
     * 可变输入输出参数类型映射表
     */
    protected Map<String, String[]> inOutDataComponentIdMap = new HashMap<>();
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


    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void functionRun(ContextOwner contextOwner) {  }

    /**
     * 此方法为组件的核心调配方法，能够让组件按照设定次序依次进行
     */
    public void verdictRun(ContextOwner contextOwner, String netComponentId) {

        //1.获取输入参数体
        DataComponent<I> inputDataComponent = null;
        if(inputDataBeanId != null){
            inputDataComponent = componentConstant.getDataComponent(inputDataBeanId, contextOwner);                //获取输入 账户/用户/上下文 下的瞬时数据组件
        }

        //2.运行业务
        System.out.println("\n组件ID：" + this.id + "，入参：" + JSON.toJSONString(inputDataComponent) + "，开始运行...");
        I inputBizData = inputDataComponent != null ? inputDataComponent.getData() : null;
        ComponentBizResult<O> componentBizResult = null;
        switch (netComponentId){
            case "D1" : componentBizResult = d1(inputBizData); break;
            case "D2" : componentBizResult = d2(inputBizData); break;
            case "D3" : componentBizResult = d3(inputBizData); break;
            case "D4" : componentBizResult = d4(inputBizData); break;
            default: componentBizResult = new ComponentBizResult<>("D_E", false, null);
        }

        //3.分析结果，保存数据
        String resultCode = componentBizResult.getResultCode();                                                         //返回执行结果码
        boolean isStore = componentBizResult.isStore();                                                                //返回输入输出类型
        O bizData = componentBizResult.getOutputData();                                                                 //返回输出数据体
        DataComponent outputDataComponent = bizData != null ? new DataComponentBase(outputDataBeanId, contextOwner, bizData.getClass().getSimpleName(), bizData) : new DataComponentBase(this.outputDataBeanId, contextOwner, null, null);
        if(isStore && outputDataBeanId != null && outputDataComponent != null){
            componentConstant.putDataComponent(outputDataComponent);                                                 //子组件的OutPutDataComponent保存到数据源ComponentConstant的Map中（后续最好用ThreadLocal实现此Map，放redis也行啊）
        }
        System.out.println("输出参数：" + JSON.toJSONString(outputDataComponent));

        if(resultCode.equals("END_S"))  return;                                                                      //流程出口（检测状态码，看是否结束）

        //4.带动下一个组件再来运行
        String nextComponentId = componentConstant.getFCIDByResultCode(resultCode);
        SemanticComponent nextComponent = componentConstant.getFunctionComponent(nextComponentId);
        System.out.println("转换 ：" + resultCode + "     --->    " + nextComponentId);
        componentConstant.setTraceByContextOwnerIndex(contextOwner.getOwnerIndex(), resultCode);

        if(nextComponent != null)  {
            if(nextComponent.getType() == 1){
                nextComponent.functionRun(contextOwner);
            }else{
                nextComponent.verdictRun(contextOwner, nextComponentId);
            }
        }

    }

    private ComponentBizResult<O> d1(I bizData) {
        System.out.println("D1 process...");
        return new ComponentBizResult<>("D1_S", false, null);
    }

    private ComponentBizResult<O> d2(I bizData) {
        System.out.println("D2 process...");
        return new ComponentBizResult<>("D2_S", false, null);
    }

    private ComponentBizResult<O> d3(I bizData) {
        System.out.println("D3 process...");
        if(true)    return new ComponentBizResult<>("D3_E", false, null);
        return new ComponentBizResult<>("D3_S", false, null);
    }

    private ComponentBizResult<O> d4(I bizData) {
        System.out.println("D4 process...");
        return new ComponentBizResult<>("D4_S", false, null);
    }
}
