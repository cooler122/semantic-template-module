package com.cooler.semantic.component.biz;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.ReferRuleRelation;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.ContextService;
import com.cooler.semantic.service.external.ReferRuleRelationService;
import com.cooler.semantic.service.internal.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("verdictComponentBase")
public class VerdictComponentBase<I> implements SemanticComponent{

    private static Logger logger = LoggerFactory.getLogger(VerdictComponentBase.class);
    /**
     * 组件ID
     */
    protected String id;

    /**
     * 组件类型（1功能组件、2判断组件）
     */
    protected int type = Constant.VERDICT_COMPONENT;
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
    private ContextService<DataComponent> contextService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ReferRuleRelationService referRuleRelationService;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void functionRun(ContextOwner contextOwner) {  }

    /**
     * 此方法为组件的核心调配方法，能够让组件按照设定次序依次进行
     */
    public void verdictRun(ContextOwner contextOwner, String componentId) {
        ComponentBizResult componentBizResult;
        switch (componentId){
            case "D1" : componentBizResult = d1(contextOwner); break;
            case "D2" : componentBizResult = d2(contextOwner); break;
            case "D3" : componentBizResult = d3(contextOwner); break;
            case "D4" : componentBizResult = d4(contextOwner); break;
            case "D6" : componentBizResult = d6(contextOwner); break;
            case "D7" : componentBizResult = d7(contextOwner); break;
            case "D8" : componentBizResult = d8(contextOwner); break;
            case "D9" : componentBizResult = d9(contextOwner); break;
            case "D10" : componentBizResult = d10(contextOwner); break;
            case "D11" : componentBizResult = d11(contextOwner); break;
            default: componentBizResult = new ComponentBizResult("D_E");
        }

        //3.分析结果，保存数据
        String resultCode = componentBizResult.getResultCode();                                                         //返回执行结果码
        int saveCode = componentBizResult.getSaveCode();                                                                //返回输入输出类型
        Object bizData = componentBizResult.getOutputData();                                                                 //返回输出数据体

        DataComponent outputDataComponent = bizData != null ? new DataComponentBase(outputDataBeanId, contextOwner, bizData.getClass().getSimpleName(), bizData) : new DataComponentBase(this.outputDataBeanId, contextOwner, null, null);
        if(outputDataBeanId != null && outputDataComponent != null){
            if(saveCode == Constant.STORE_LOCAL){                                                                     //将输出对象保存到本地
                componentConstant.putDataComponent(outputDataComponent);                                                    //子组件的OutPutDataComponent保存到数据源ComponentConstant的Map中（后续最好用ThreadLocal实现此Map，放redis也行啊）
            }else if(saveCode == Constant.STORE_REMOTE){                                                             //将输出对象保存到远程
                contextService.setContext(contextOwner.getOwnerIndex() + "_" + outputDataBeanId, outputDataComponent);
            }else if(saveCode == Constant.STORE_LOCAL_REMOTE){                                                      //将输出对象保存到本地和远程
                componentConstant.putDataComponent(outputDataComponent);
                contextService.setContext(contextOwner.getOwnerIndex() + "_" + outputDataBeanId, outputDataComponent);
            } else if (saveCode == Constant.STORE_LOCAL_REMOTE_CONTEXTLOG){
                componentConstant.putDataComponent(outputDataComponent);
                contextService.setContext(contextOwner.getOwnerIndex() + "_" + outputDataBeanId, outputDataComponent, true);
            }
        }
        componentConstant.setTraceByContextOwnerIndex(contextOwner.getOwnerIndex(), resultCode);
        if(resultCode.equals("END_S"))  return;                                                                      //流程出口（检测状态码，看是否结束）

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

    private ComponentBizResult d1(ContextOwner contextOwner) {
        DataComponent semanticParserRequest = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = (SemanticParserRequest)semanticParserRequest.getData();
        int lastState = request.getLastState();
        if(lastState < 0){
            return new ComponentBizResult("D1_Y");
        }else{
            return new ComponentBizResult("D1_N");
        }
    }

    private ComponentBizResult d2(ContextOwner contextOwner) {
        DataComponent<SVRuleInfo> dataComponent = componentConstant.getDataComponent("optimalSvRuleInfo_LPM", contextOwner);
        if(dataComponent != null && dataComponent.getData() != null){                                                 //缺参上下文既然形成了上下文，那么当前相似度值必然大于阈值的
            return new ComponentBizResult("D2_Y");
        }else{
            return new ComponentBizResult("D2_N");
        }
    }

    private ComponentBizResult d3(ContextOwner contextOwner) {
        DataComponent<SVRuleInfo> dataComponent = componentConstant.getDataComponent("optimalSvRuleInfo_CPM", contextOwner);
        if(dataComponent != null && dataComponent.getData() != null){
            SVRuleInfo changeParamOptimalSvRuleInfo = dataComponent.getData();
            if(changeParamOptimalSvRuleInfo.isEnsureFinal()){                                                           //判断换参匹配结果是否确定为最终最优结果
                return new ComponentBizResult("D3_Y");
            }else{
                return new ComponentBizResult("D3_N");
            }
        }
        return new ComponentBizResult("D3_N");
    }

    private ComponentBizResult<SVRuleInfo> d4(ContextOwner contextOwner) {
        DataComponent<SVRuleInfo> dataComponent = componentConstant.getDataComponent("optimalSvRuleInfo", contextOwner);
        if(dataComponent != null && dataComponent.getData() != null){
            SVRuleInfo svRuleInfo = dataComponent.getData();
            Double similarity = svRuleInfo.getSimilarity();                                                             //相似度值
            Double runningAccuracyThreshold = svRuleInfo.getRunningAccuracyThreshold();                                 //运行中阈值
            if(similarity >= runningAccuracyThreshold){
                return new ComponentBizResult("D4_Y");
            }else{
                return new ComponentBizResult("D4_N");
            }
        }else{
            return new ComponentBizResult("D4_N");
        }
    }

    private ComponentBizResult d6(ContextOwner contextOwner) {
        DataComponent<SVRuleInfo> optimalSvRuleInfo = componentConstant.getDataComponent("optimalSvRuleInfo", contextOwner);
        if(optimalSvRuleInfo != null){
            SVRuleInfo svRuleInfo = optimalSvRuleInfo.getData();
            List<RRuleEntity> lackedRRuleEntities = svRuleInfo.getLackedRRuleEntities();
            if(lackedRRuleEntities != null && lackedRRuleEntities.size() > 0){                                         //lackedRRuleEntities里面的RRE必须都是必须实体
                return new ComponentBizResult("D6_N");
            }
        }
        return new ComponentBizResult("D6_Y");
    }

    private ComponentBizResult d7(ContextOwner contextOwner) {
        DataComponent<SVRuleInfo> optimalSvRuleInfo = componentConstant.getDataComponent("optimalSvRuleInfo", contextOwner); //此时optimalSvRuleInfo一定不为空
        SVRuleInfo svRuleInfo = optimalSvRuleInfo.getData();
        Integer accountId = svRuleInfo.getAccountId();

        Integer ruleId = svRuleInfo.getRuleId();
        Integer guideIntentId = svRuleInfo.getGuideIntentId();
        if(guideIntentId == null){                                                                                     //第一次进入引导规则，这个引导意图ID是没有的，需要取出引导规则的意图ID
            guideIntentId = ruleService.selectIntentId(ruleId);
        }
        List<ReferRuleRelation> referRuleRelations = referRuleRelationService.selectByRIdAId(ruleId, guideIntentId, accountId);
        if(referRuleRelations == null || referRuleRelations.size() == 0){
            return new ComponentBizResult("D7_N");
        }else{
            svRuleInfo.setIsLongConversationRule(true);                                                                  //此处设置此SVRuleInfo是长对话规则
            return new ComponentBizResult("D7_Y");
        }
    }

    private ComponentBizResult d8(ContextOwner contextOwner) {
        if(true){                                                                                                      //TODO;匹配的意图是否是运行中意图
            return new ComponentBizResult("D8_N");
        }else{
            return new ComponentBizResult("D8_Y");
        }
    }

    private ComponentBizResult d9(ContextOwner contextOwner) {
        DataComponent semanticParserRequest = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = (SemanticParserRequest)semanticParserRequest.getData();
        int lastState = request.getLastState();
//        DataComponent<SVRuleInfo> optimalSvRuleInfo_DataComponent = componentConstant.getDataComponent("optimalSvRuleInfo", contextOwner);
//        boolean haveResult = optimalSvRuleInfo_DataComponent != null && optimalSvRuleInfo_DataComponent.getData() != null;
//        if(haveResult) {       //（有提示词本）轮还是有结果的，但结果全部小于阈值，因为当前D4->D9，如果结果大于阈值的就会从D4->D6了
//            if(lastState < 0){
//                //去LRRC，再做最后一搏，但将规则问出来 ---> 回应“不知道您上句话什么意思，如果你是问${ruleName}，那么您可以这样问${ruleTemplate}，您前面问的是...呢？（保存缺参上下文）
//                return new ComponentBizResult("D9_Y_Y");
//            }else{
//                //去失败结果体，但将规则问出来 ---> 回应“不知道您上句话什么意思，如果你是问${ruleName}，那么您可以这样问${ruleTemplate}（保存当前上下文，虽然失败结果）
//                return new ComponentBizResult("D9_Y_N");
//            }
//        }else{                                                                                                     //（无提示词）本轮结果彻底没解析出来，为空，不用谈阈值了
//            if(lastState < 0){
//                //去LRRC了，再做最后一搏，看看能否上轮缺参问题回退一步 ---> 回应“不知道您上句话的意思，您前面问的是...呢？”（保存回退的缺参上下文）
//                return new ComponentBizResult("D9_N_Y");
//            }else{
//                //完全要去失败结果体了--->回应完全不知道您说什么（保存当前的空上下文）
//                return new ComponentBizResult("D9_N_N");
//            }
//        }

        if(lastState < 0){
            //去LRRC，再做最后一搏，但将规则问出来 ---> 回应“不知道您上句话什么意思，如果你是问${ruleName}，那么您可以这样问${ruleTemplate}，您前面问的是...呢？（保存缺参上下文）
            return new ComponentBizResult("D9_Y");
        }else{
            //去失败结果体，但将规则问出来 ---> 回应“不知道您上句话什么意思，如果你是问${ruleName}，那么您可以这样问${ruleTemplate}（保存当前上下文，虽然失败结果）
            return new ComponentBizResult("D9_N");
        }
    }

    private ComponentBizResult d10(ContextOwner contextOwner) {                                                     //TODO:这一个还是要放一些判断逻辑的，但是用不用积累询问次数，现在还不能确定，以后的测试看效果吧
        if(true){
            return new ComponentBizResult("D10_N");
        }else{
            return new ComponentBizResult("D10_Y");
        }
    }

    private ComponentBizResult d11(ContextOwner contextOwner) {
        DataComponent semanticParserRequest = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = (SemanticParserRequest)semanticParserRequest.getData();

        int accumulatedQueryCount = request.getAccumulatedQueryCount();
        int entityMaxQueryCount = request.getEntityMaxQueryCount();
        long now = System.currentTimeMillis();
        long lastEndTimestamp = request.getLastEndTimestamp();
        int contextWaitTime = request.getContextWaitTime();

        if(accumulatedQueryCount >= entityMaxQueryCount || now - lastEndTimestamp > contextWaitTime){
            return new ComponentBizResult("D11_Y");
        }else{
            return new ComponentBizResult("D11_N");
        }
    }
}
