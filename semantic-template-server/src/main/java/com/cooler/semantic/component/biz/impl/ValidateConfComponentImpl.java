package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.component.ComponentBizResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("validateConfComponent")
public class ValidateConfComponentImpl extends FunctionComponentBase<SemanticParserRequest, SemanticParserRequest> {

    private static Logger logger = LoggerFactory.getLogger(ValidateConfComponentImpl.class.getName());

    public ValidateConfComponentImpl() {
        super("VCC", "SO_1", "semanticParserRequest", "semanticParserRequest");
    }

    @Override
    protected ComponentBizResult<SemanticParserRequest> runBiz(SemanticParserRequest bizData) {
        logger.info("SO_1.校验和配置");

        if(!validate(bizData))  return new ComponentBizResult("VCC_E", false, null);
        if(!checkIn(bizData))   return new ComponentBizResult("VCC_E", false, null);
        setAccountData(bizData);

        return new ComponentBizResult("VCC_S", true, bizData);
    }



    private boolean validate(SemanticParserRequest request) {
        logger.info("SO-1-1.请求体校验...");
        //TODO:请求体校验
        return true;
    }


    private boolean checkIn(SemanticParserRequest request) {
        logger.info("SO-1-2.账户权限校验...");
        //TODO:账户权限校验
        return true;
    }


    private void setAccountData(SemanticParserRequest request) {
        logger.info("SO-1-3.账户自定义参数设置...");
        //TODO:账户自定义参数设置
    }
}
