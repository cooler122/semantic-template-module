package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("lackResultRetrocessionComponent")
public class LackResultRetrocessionComponentImpl extends FunctionComponentBase<SVRuleInfo, SVRuleInfo> {

    @Autowired
    private RedisService<SVRuleInfo> redisService;

    public LackResultRetrocessionComponentImpl() {
        super("LRRC", "SO-15", "optimalSvRuleInfo", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<SVRuleInfo> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        Integer contextId = contextOwner.getContextId();
        SVRuleInfo optimalSvRuleInfo = svRuleInfo;                                                                      //先不管当前会话是否得到最优结果，先准备一个返回体。
        if(optimalSvRuleInfo == null){                                                                                 //当前返回体没有值，就要给它准备一个值，就从历史结果中找一个值
            for(int i = 1; i <= 5; i ++){
                String lastI_OwnerIndex = contextOwner.getLastNOwnerIndex(i);
                DataComponentBase<SVRuleInfo> historyData = redisService.getCacheObject(lastI_OwnerIndex + "_" + "optimalSvRuleInfo");
                if(historyData != null && historyData.getData() != null){
                    optimalSvRuleInfo = historyData.getData();
                    List<REntityWordInfo> matchedREntityWordInfos = optimalSvRuleInfo.getMatchedREntityWordInfos();     //将所有匹配上的REWI修改对话编号，变为本轮对话编号后进行存储
                    for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                        matchedREntityWordInfo.setContextId(contextId);
                    }
                    break;                                                                                             //找到最近的一个历史数据记录即可，就不用找其他更远的版本了
                }
            }
        }
        if(optimalSvRuleInfo != null){                                                                                 //如果当前有了值，则返回成功
            return new ComponentBizResult("LRRC_S", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);
        }else{                                                                                                          //如果入参为空，经过上面历史赋值都失败了，那么就返回失败
            return new ComponentBizResult("LRRC_F");
        }

    }
}
