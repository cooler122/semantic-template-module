package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.*;

@Component("changeParamMatchComponent")
public class ChangeParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(ChangeParamMatchComponentImpl.class.getName());

    public ChangeParamMatchComponentImpl() {
        super("CPMC", "SO-6 ~ SO-7", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("changeParamMatch.换参匹配");
        SVRuleInfo optimalSvRuleInfo = null;

        return new ComponentBizResult("FPMC_S", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);      //此结果在本地和远程都要存储
    }
}
