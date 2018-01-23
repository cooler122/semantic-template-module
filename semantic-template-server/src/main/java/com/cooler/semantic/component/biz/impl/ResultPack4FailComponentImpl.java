package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("resultPack4FailComponent")
public class ResultPack4FailComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {

    private static Logger logger = LoggerFactory.getLogger(ResultPack4FailComponentImpl.class.getName());

    public ResultPack4FailComponentImpl() {
        super("RPC4F", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("RPC4F.失败结果包装");
        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();

        String sentence = request.getCmd();
        Double similarity = 0d;
        long responseTimestamp = System.currentTimeMillis();

        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setResponseType(Constant.FAIL_RESULT);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(0);

        return new ComponentBizResult("RPC4F_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
