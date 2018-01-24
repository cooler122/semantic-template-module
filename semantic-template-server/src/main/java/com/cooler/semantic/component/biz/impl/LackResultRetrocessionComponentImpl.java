package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("lackResultRetrocessionComponent")
public class LackResultRetrocessionComponentImpl extends FunctionComponentBase<SVRuleInfo, SVRuleInfo> {

    private static Logger logger = LoggerFactory.getLogger(LackResultRetrocessionComponentImpl.class.getName());

    public LackResultRetrocessionComponentImpl() {
        super("LRRC", "optimalSvRuleInfo", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<SVRuleInfo> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("LRRC.缺参结果回退");
        if(svRuleInfo != null){
            return new ComponentBizResult("LRRC_S1");
        }
        DataComponent<List<DataComponent<SVRuleInfo>>> historyDataComponent = componentConstant.getDataComponent("historyDataComponents", contextOwner);
        List<DataComponent<SVRuleInfo>> historyDataComponents = historyDataComponent.getData();

        Integer contextId = contextOwner.getContextId();
        SVRuleInfo optimalSvRuleInfo = svRuleInfo;                                                                      //先不管当前会话是否得到最优结果，先准备一个返回体。
        for(DataComponent<SVRuleInfo> historyData : historyDataComponents){
            if(historyData != null && historyData.getData() != null){
                optimalSvRuleInfo = historyData.getData();
                List<REntityWordInfo> matchedREntityWordInfos = optimalSvRuleInfo.getMatchedREntityWordInfos();         //将所有匹配上的REWI修改对话编号，变为本轮对话编号后进行存储
                for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                    matchedREntityWordInfo.setContextId(contextId);
                }
                break;                                                                                                 //找到最近的一个历史数据记录即可，就不用找其他更远的版本了
            }
        }

        if(optimalSvRuleInfo != null){                                                                                 //如果当前有了值，则返回成功
            return new ComponentBizResult("LRRC_S2", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);
        }else{                                                                                                          //如果入参为空，经过上面历史赋值都失败了，那么就返回失败
            return new ComponentBizResult("LRRC_F");
        }
    }
}
