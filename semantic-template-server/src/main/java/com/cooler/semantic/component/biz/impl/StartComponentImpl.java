package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component("startComponent")
public class StartComponentImpl extends FunctionComponentBase<Object, Object> {

    private static Logger logger = LoggerFactory.getLogger(StartComponentImpl.class.getName());

    @Autowired
    private RedisService<DataComponent<SVRuleInfo>> redisService;

    public StartComponentImpl() {
        super("STARTC", null, "historyDataComponents");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, Object bizData) {
        logger.trace("STARTC.开始...");

        DataComponent<SemanticParserRequest> semanticParserRequestDataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest semanticParserRequest = semanticParserRequestDataComponent.getData();
        int memorizedConversationCount = semanticParserRequest.getMemorizedConversationCount();                          //从本地缓存中取出用户设置的对话记忆轮次

        //1.准备好5轮的历史数据
        List<String> lastNDataNames = new ArrayList<>();                                                                 //构建好对话多轮次的key
        for(int i = 1; i <= memorizedConversationCount; i ++){
            String lastIDataName = contextOwner.getLastNData(i, "optimalSvRuleInfo");
            lastNDataNames.add(lastIDataName);
        }

        List<DataComponent<SVRuleInfo>> historyDataComponents = redisService.getCacheObjects(lastNDataNames);           //一次查询出memorizedConversationCount个历史记录
        if(historyDataComponents != null && historyDataComponents.size() > 0){
            Collections.sort(historyDataComponents, new Comparator<DataComponent<SVRuleInfo>>() {
                @Override
                public int compare(DataComponent<SVRuleInfo> o1, DataComponent<SVRuleInfo> o2) {                        //按 contextId 大小来降序排列
                    if(o1 != null && o2 != null){
                        Integer contextId1 = o1.getContextOwner().getContextId();
                        Integer contextId2 = o2.getContextOwner().getContextId();
                        if(contextId1 > contextId2){
                            return -1;
                        }else if(contextId1 < contextId2){
                            return 1;
                        }
                    }
                    return 0;
                }
            });
        }
        return new ComponentBizResult("START", Constant.STORE_LOCAL_REMOTE, historyDataComponents);
    }
}
