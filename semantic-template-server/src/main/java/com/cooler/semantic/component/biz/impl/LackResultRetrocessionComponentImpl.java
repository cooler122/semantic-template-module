package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.internal.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("lackResultRetrocessionComponent")
public class LackResultRetrocessionComponentImpl extends FunctionComponentBase<SVRuleInfo, SVRuleInfo> {

    private static Logger logger = LoggerFactory.getLogger(LackResultRetrocessionComponentImpl.class.getName());
    @Autowired
    private RuleService ruleService;
    public LackResultRetrocessionComponentImpl() {
        super("LRRC", "optimalSvRuleInfo", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<SVRuleInfo> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("LRRC.缺参结果回退");
        if(svRuleInfo != null && svRuleInfo.getSimilarity() >= svRuleInfo.getRunningAccuracyThreshold()){               //如果svRuleInfo不为空并且svRuleInfo有效，那么无需缺参上下文回退
            return new ComponentBizResult("LRRC_S_N");
        }
        if(svRuleInfo != null && svRuleInfo.getSimilarity() < svRuleInfo.getRunningAccuracyThreshold()){               //用户的句子没有匹配上，但可以设置提示句子
            String sentence = svRuleInfo.getSentence();
            Integer ruleId = svRuleInfo.getRuleId();
            Rule rule = ruleService.selectByPrimaryKey(ruleId);
            String sugguestData = "抱歉，您问的 '" + sentence + "'这句话不够具体，如果您是要问：" + rule.getRuleMsg() + "，那你可以换这种模式问：" + rule.getRuleTemplate();
            componentConstant.putDataComponent(new DataComponentBase("sugguestData", contextOwner, "String", sugguestData));
        }
        Integer contextId = contextOwner.getContextId();
        DataComponent<List<DataComponent<SVRuleInfo>>> historyDataComponent = componentConstant.getDataComponent("historyDataComponents", contextOwner);
        List<DataComponent<SVRuleInfo>> historyDataComponents = historyDataComponent.getData();
        DataComponent<SVRuleInfo> lastHistoryDataComponent = historyDataComponents.get(0);                              //TODO:要检查，不知道这个结果是不是上一轮的，还不知道这个集合排序了没有

        SVRuleInfo optimalSvRuleInfo = null;
        if(lastHistoryDataComponent != null && lastHistoryDataComponent.getData() != null){
            optimalSvRuleInfo = lastHistoryDataComponent.getData();
            List<REntityWordInfo> matchedREntityWordInfos = optimalSvRuleInfo.getMatchedREntityWordInfos();             //将所有匹配上的REWI修改对话编号，变为本轮对话编号后进行存储
            for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                matchedREntityWordInfo.setContextId(contextId);
            }
        }

        if(optimalSvRuleInfo != null){                                                                                 //走到这里，说明本轮结果为空或者小于阈值，则准备好上一轮的缺参上下文进行回退
            return new ComponentBizResult("LRRC_S_Y", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);
        }else{                                                                                                          //如果入参结果为空或者小于阈值，而且经过上面历史赋值都失败了都不能回退，那么就只能返回失败了
            return new ComponentBizResult("LRRC_F");
        }
    }
}
