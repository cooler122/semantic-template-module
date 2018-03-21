package com.cooler.semantic.component.biz;

import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.data.*;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.service.external.ContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FunctionComponentBase<I, O> implements SemanticComponent {
    private static Logger logger = LoggerFactory.getLogger(FunctionComponentBase.class);
    /**
     * 组件ID
     */
    protected String id;
    /**
     * 组件类型（1功能组件、2判断组件）
     */
    protected int type = Constant.FUNCTION_COMPONENT;
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
    protected ComponentConstant componentConstant;

    @Autowired
    private ContextService<DataComponent> contextService;

    public FunctionComponentBase(String id, String inputDataBeanId, String outputDataBeanId) {
        this.id = id;
        this.type = Constant.FUNCTION_COMPONENT;
        this.inputDataBeanId = inputDataBeanId;
        this.outputDataBeanId = outputDataBeanId;
    }

    public int getType() {
        return type;
    }

    /**
     * 此方法为组件的核心调配方法，能够让组件按照设定次序依次进行
     */
    public void functionRun(ContextOwner contextOwner) {

        //1.获取输入参数体
        DataComponent<I> inputDataComponent = null;
        if(inputDataBeanId != null){
            inputDataComponent = componentConstant.getDataComponent(inputDataBeanId, contextOwner);                //获取输入 账户/用户/上下文 下的瞬时数据组件
        }

        //2.运行业务
        I inputBizData = inputDataComponent != null ? inputDataComponent.getData() : null;
        ComponentBizResult<O> componentBizResult = runBiz(contextOwner, inputBizData);                                                //运行子组件的逻辑，运行体中获得子组件的OutPutDataComponent

        //3.分析结果，保存数据
        String resultCode = componentBizResult.getResultCode();                                                         //返回执行结果码
        int saveCode = componentBizResult.getSaveCode();                                                                //返回输入输出类型
        O bizData = componentBizResult.getOutputData();                                                                 //返回输出数据体
        DataComponent outputDataComponent = bizData != null ? new DataComponentBase(outputDataBeanId, contextOwner, bizData.getClass().getSimpleName(), bizData) : new DataComponentBase(this.outputDataBeanId, contextOwner, null, null);
        if(outputDataBeanId != null && outputDataComponent != null){
            //1.存储策略执行
            if(saveCode == Constant.STORE_LOCAL){                                                                     //将输出对象保存到本地
                componentConstant.putDataComponent(outputDataComponent);                                                    //子组件的OutPutDataComponent保存到数据源ComponentConstant的Map中（后续最好用ThreadLocal实现此Map，放redis也行啊）
            }else if(saveCode == Constant.STORE_REMOTE){                                                             //将输出对象保存到远程
                contextService.setCacheObject(contextOwner.getOwnerIndex() + "_" + outputDataBeanId, outputDataComponent);
            }else if(saveCode == Constant.STORE_LOCAL_REMOTE){                                                      //将输出对象保存到本地和远程
                componentConstant.putDataComponent(outputDataComponent);
                contextService.setCacheObject(contextOwner.getOwnerIndex() + "_" + outputDataBeanId, outputDataComponent);
            }

            //2.日志存储
        }
        componentConstant.setTraceByContextOwnerIndex(contextOwner.getOwnerIndex(), resultCode);
        if(resultCode.equals("END_S"))  return;                                                                       //流程出口（检测状态码，看是否结束）

        //4.带动下一个组件再来运行
        String nextComponentId = componentConstant.getFCIDByResultCode(resultCode);
        SemanticComponent nextComponent = componentConstant.getFunctionComponent(nextComponentId);

        if(nextComponent != null)  {
            if(nextComponent.getType() == Constant.FUNCTION_COMPONENT){
                nextComponent.functionRun(contextOwner);
            }else{
                nextComponent.verdictRun(contextOwner, nextComponentId);
            }
        }
    }

    @Override
    public void verdictRun(ContextOwner contextOwner, String nextComponentId) {  }

    /**
     * 本组件需要运行的业务逻辑，由本抽象类的具体子类来重写实现
     * @return  返回运行的结果体以及结果状态
     */
    protected ComponentBizResult<O> runBiz(ContextOwner contextOwner, I bizData) {
        return null;
    }

}
