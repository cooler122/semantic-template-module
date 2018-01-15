package com.cooler.semantic.component.biz;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.RedisService;
import com.cooler.semantic.service.internal.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private RedisService<DataComponent> redisService;

    @Autowired
    private RuleService ruleService;

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void functionRun(ContextOwner contextOwner) {  }

    /**
     * 此方法为组件的核心调配方法，能够让组件按照设定次序依次进行
     */
    public void verdictRun(ContextOwner contextOwner, String componentId) {

        ComponentBizResult<O> componentBizResult;
        switch (componentId){
            case "D1" : componentBizResult = d1(contextOwner); break;
            case "D2" : componentBizResult = d2(contextOwner); break;
            case "D3" : componentBizResult = d3(); break;
            case "D4" : componentBizResult = d4(); break;
            case "D6" : componentBizResult = d6(contextOwner); break;
            case "D7" : componentBizResult = d7(contextOwner); break;
            case "D8" : componentBizResult = d8(contextOwner); break;
            case "D9" : componentBizResult = d9(contextOwner); break;
            case "D11" : componentBizResult = d11(contextOwner); break;
            default: componentBizResult = new ComponentBizResult<>("D_E");
        }

        //3.分析结果，保存数据
        String resultCode = componentBizResult.getResultCode();                                                         //返回执行结果码
        int saveCode = componentBizResult.getSaveCode();                                                                //返回输入输出类型
        O bizData = componentBizResult.getOutputData();                                                                 //返回输出数据体

        DataComponent outputDataComponent = bizData != null ? new DataComponentBase(outputDataBeanId, contextOwner, bizData.getClass().getSimpleName(), bizData) : new DataComponentBase(this.outputDataBeanId, contextOwner, null, null);
        if(outputDataBeanId != null && outputDataComponent != null){
            if(saveCode == Constant.STORE_LOCAL){                                                                     //将输出对象保存到本地
                componentConstant.putDataComponent(outputDataComponent);                                                    //子组件的OutPutDataComponent保存到数据源ComponentConstant的Map中（后续最好用ThreadLocal实现此Map，放redis也行啊）
            }else if(saveCode == Constant.STORE_REMOTE){                                                             //将输出对象保存到远程
                redisService.setCacheObject(contextOwner.getOwnerIndex() + "_" + outputDataBeanId, outputDataComponent);
            }else if(saveCode == Constant.STORE_LOCAL_REMOTE){                                                      //将输出对象保存到本地和远程
                componentConstant.putDataComponent(outputDataComponent);
                redisService.setCacheObject(contextOwner.getOwnerIndex() + "_" + outputDataBeanId, outputDataComponent);
            }
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

    private ComponentBizResult<O> d1(ContextOwner contextOwner) {
        System.out.println("D1 process...");
        DataComponent semanticParserRequest = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = (SemanticParserRequest)semanticParserRequest.getData();
        int lastState = request.getLastState();
        if(lastState < 0){
            return new ComponentBizResult<>("D1_Y");
        }else{
            return new ComponentBizResult<>("D1_N");
//            return new ComponentBizResult<>("D1_Y");                                                         //TODO：此行是测试代码，为了测试缺参匹配后面要删除此行
        }
    }

    private ComponentBizResult<O> d2(ContextOwner contextOwner) {
        System.out.println("D2 process...");
        DataComponent optimalSvRuleInfo = componentConstant.getDataComponent("optimalSvRuleInfo", contextOwner);
        if(optimalSvRuleInfo == null){
            return new ComponentBizResult<>("D2_Y");
        }else{
            return new ComponentBizResult<>("D2_N");
        }
    }

    private ComponentBizResult<O> d3() {
        System.out.println("D3 process...");
//        if(true)    return new ComponentBizResult<>("D3_E", false, null);
        return new ComponentBizResult<>("D3_N");
    }

    private ComponentBizResult<O> d4() {
        System.out.println("D4 process...");
        return new ComponentBizResult<>("D4_S");
    }

    private ComponentBizResult<O> d6(ContextOwner contextOwner) {
        System.out.println("D6 process...");
        DataComponent<SVRuleInfo> optimalSvRuleInfo = componentConstant.getDataComponent("optimalSvRuleInfo", contextOwner);
        if(optimalSvRuleInfo != null){
            SVRuleInfo svRuleInfo = optimalSvRuleInfo.getData();
            List<RRuleEntity> lackedRRuleEntities = svRuleInfo.getLackedRRuleEntities();
            for (RRuleEntity lackedRRuleEntity : lackedRRuleEntities) {
                Byte isNecessary = lackedRRuleEntity.getIsNecessary();
                if(isNecessary == 1){                                                                                   //校验缺失参数中是否有参数为必须匹配的参数
                    return new ComponentBizResult<>("D6_N");
                }
            }
        }
        return new ComponentBizResult<>("D6_Y");
    }

    private ComponentBizResult<O> d7(ContextOwner contextOwner) {
        System.out.println("D7 process...");
        DataComponent<SVRuleInfo> optimalSvRuleInfo = componentConstant.getDataComponent("optimalSvRuleInfo", contextOwner);
        SVRuleInfo svRuleInfo = optimalSvRuleInfo.getData();
        Integer ruleId = svRuleInfo.getRuleId();
        Rule rule = ruleService.selectByPrimaryKey(ruleId);                      //TODO:这一部分还是放到外边的业务组件里面，将rule保存起来，这里就接受一个值即可
        Integer referRuleId = rule.getReferRuleId();
        if(referRuleId.intValue() == 0){
            return new ComponentBizResult<>("D7_N");
        }else{
            return new ComponentBizResult<>("D7_Y");
        }
    }

    private ComponentBizResult<O> d8(ContextOwner contextOwner) {
        System.out.println("D8 process...");
        return new ComponentBizResult<>("D8_N");
    }

    private ComponentBizResult<O> d9(ContextOwner contextOwner) {
        System.out.println("D9 process...");
        DataComponent semanticParserRequest = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = (SemanticParserRequest)semanticParserRequest.getData();
        int lastState = request.getLastState();
        if(lastState < 0){
            return new ComponentBizResult<>("D9_Y");
        }else{
            return new ComponentBizResult<>("D9_N");
        }
    }

    private ComponentBizResult<O> d11(ContextOwner contextOwner) {
        System.out.println("D11 process...");
        DataComponent semanticParserRequest = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = (SemanticParserRequest)semanticParserRequest.getData();

        int accumulatedQueryCount = request.getAccumulatedQueryCount();
        int entityMaxQueryCount = request.getEntityMaxQueryCount();
        long now = System.currentTimeMillis();
        long lastEndTimestamp = request.getLastEndTimestamp();
        int contextWaitTime = request.getContextWaitTime();

        if(accumulatedQueryCount >= entityMaxQueryCount || now - lastEndTimestamp > contextWaitTime){
            return new ComponentBizResult<>("D11_Y");
        }else{
            return new ComponentBizResult<>("D11_N");
        }
    }
}
