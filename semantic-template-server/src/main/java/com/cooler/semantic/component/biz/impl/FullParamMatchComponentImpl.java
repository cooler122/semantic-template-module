package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.RuleSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("fullParamMatchComponent")
public class FullParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(FullParamMatchComponentImpl.class.getName());

    @Autowired
    private RuleSearchService ruleSearchService;

    public FullParamMatchComponentImpl() {
        super("FPMC", "SO-8 ~ SO-10", "sentenceVectors", "rulesBySentenceVectors");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("fullParamMatch.全参匹配");

        System.out.println(JSON.toJSONString(sentenceVectors));
        Integer accountId = contextOwner.getAccountId();
        List<Rule> rulesBySentenceVectors = ruleSearchService.getRulesBySentenceVectors(accountId, sentenceVectors);

        System.out.println(JSON.toJSONString(rulesBySentenceVectors));

        return new ComponentBizResult("FPMC_S",1, rulesBySentenceVectors);
    }

}
