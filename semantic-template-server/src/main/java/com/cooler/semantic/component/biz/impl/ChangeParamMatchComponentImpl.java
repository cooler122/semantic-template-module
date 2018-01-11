package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
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
        Map<Integer, DataComponentBase<SVRuleInfo>> historyDataComponentMap = new HashMap<>();
        for(int i = 0;i < 5; i ++){                                                                                    //先查询出历史数据
            String lastI_OwnerIndex = contextOwner.getLastNOwnerIndex(i);
            DataComponentBase<SVRuleInfo> historyData = redisService.getCacheObject(lastI_OwnerIndex + "_" + "optimalSvRuleInfo");//TODO：以后看看能否5次放到一起查出来
            if(historyData != null){
                historyDataComponentMap.put(i, historyData);
            }
        }

        for(int i = 0; i < 5; i ++){

        }

        SVRuleInfo optimalSvRuleInfo = null;

        return new ComponentBizResult("FPMC_S", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);      //此结果在本地和远程都要存储
    }
}
