package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.internal.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("resultPack4FailComponent")
public class ResultPack4FailComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {

    private static Logger logger = LoggerFactory.getLogger(ResultPack4FailComponentImpl.class.getName());
    @Autowired
    private RuleService ruleService;

    public ResultPack4FailComponentImpl() {
        super("RPC4F", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("RPC4F.失败结果包装");

        DataComponent<SemanticParserRequest> semanticParserRequestDataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = semanticParserRequestDataComponent.getData();
        String sentence = request.getCmd();
        String responseMsg = "抱歉，没解析出你说的 '" + sentence + "' 这句话！";
        Double similarity = 0d;
        if(svRuleInfo != null){
            sentence = svRuleInfo.getSentence();
            similarity = svRuleInfo.getSimilarity();
            similarity = new BigDecimal(similarity).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            Integer ruleId = svRuleInfo.getRuleId();
            Rule rule = ruleService.selectByPrimaryKey(ruleId);
            responseMsg = "抱歉，您问的 '" + sentence + "'这句话不够具体，如果您是要问：" + rule.getRuleMsg() + "，那你可以换这种模式问：" + rule.getRuleTemplate();
        }
        long responseTimestamp = System.currentTimeMillis();
        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setAccountIds(contextOwner.getAccountIds());
        semanticParserResponse.setUserId(contextOwner.getUserId());
        semanticParserResponse.setContextId(contextOwner.getContextId());
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setResponseType(Constant.FAIL_RESULT);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(0);
        semanticParserResponse.setResponseMsg(responseMsg);

        return new ComponentBizResult("RPC4F_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
