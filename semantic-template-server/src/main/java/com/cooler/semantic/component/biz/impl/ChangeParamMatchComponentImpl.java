package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.model.*;
import com.cooler.semantic.service.external.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component("changeParamMatchComponent")
public class ChangeParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(ChangeParamMatchComponentImpl.class.getName());
    @Autowired
    private RedisService<SVRuleInfo> redisService;

    public ChangeParamMatchComponentImpl() {
        super("CPMC", "SO-6 ~ SO-7", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("changeParamMatch.换参匹配");



        //1.准备好5轮的历史数据
        Map<String, REntityWordInfo> historyREWIMap = new HashMap<>();
        for(int i = 0;i < 5; i ++){                                                                                     //先查询出历史数据
            String lastI_OwnerIndex = contextOwner.getLastNOwnerIndex(i);
            DataComponentBase<SVRuleInfo> historyData = redisService.getCacheObject(lastI_OwnerIndex + "_" + "optimalSvRuleInfo");//TODO：以后看看能否5次放到一起查出来
            if(historyData != null){
                SVRuleInfo svRuleInfo = historyData.getData();
                List<List<REntityWordInfo>> REntityWordInfosList = svRuleInfo.getrEntityWordInfosList();
                for (List<REntityWordInfo> rEntityWordInfos : REntityWordInfosList) {
                    for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                        String entityTypeId = rEntityWordInfo.getEntityTypeId();
                        historyREWIMap.put(entityTypeId, rEntityWordInfo);
                    }
                }
            }
        }

        for (SentenceVector sentenceVector : sentenceVectors) {
            List<List<REntityWordInfo>> rEntityWordInfosList = sentenceVector.getrEntityWordInfosList();
            for (List<REntityWordInfo> rEntityWordInfos : rEntityWordInfosList) {
                for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                    String entityTypeId = rEntityWordInfo.getEntityTypeId();
                    REntityWordInfo hitREntityWordInfo = historyREWIMap.get(entityTypeId);
                    if(hitREntityWordInfo != null){

                    }
                }
            }
        }



        for(int i = 0; i < 2; i ++){                                                                                    //换参匹配，规定换参上下文只能2轮内进行换参//TODO：实际可以一轮
//            DataComponentBase<SVRuleInfo> svRuleInfoDataComponentBase = historyDataComponentMap.get(i);
//            SVRuleInfo historySVRuleInfo = svRuleInfoDataComponentBase.getData();
//            List<REntityWordInfo> matchedREntityWordInfos = historySVRuleInfo.getMatchedREntityWordInfos();

        }

        SVRuleInfo optimalSvRuleInfo = null;

        return new ComponentBizResult("FPMC_S", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);             //此结果在本地和远程都要存储
    }
}
