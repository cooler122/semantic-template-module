package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.biz.ValidateConfComponent;
import com.cooler.semantic.component.data.DataComponent;

import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.IOType;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.component.ComponentBizResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("validateConfComponent")
public class ValidateConfComponentImpl extends FunctionComponentBase implements ValidateConfComponent {

    private static Logger logger = LoggerFactory.getLogger(ValidateConfComponentImpl.class.getName());

    public ValidateConfComponentImpl() {
        super("VCC", "SO-1", "semanticParserRequest", "semanticParserRequest");
    }

    @Override
    protected ComponentBizResult runBiz(DataComponent inputDataComponent) {
        logger.info("SO_1.校验和配置");

        SemanticParserRequest request = (SemanticParserRequest)inputDataComponent.getData();

        if(!validate(request))  return new ComponentBizResult("VCC_E", IOType.NO_IN_OUT, null);
        if(!checkIn(request))   return new ComponentBizResult("VCC_E", IOType.NO_IN_OUT, null);
        setAccountData(request);


        DataComponent outputDataComponent = new DataComponentBase("semanticParserRequest", "SemanticParserRequest", request);
        return new ComponentBizResult("VCC_S", IOType.IN_OUT, outputDataComponent);
    }



    private boolean validate(SemanticParserRequest request) {
        logger.info("SO-1-1.请求体校验...");
        return true;
    }


    private boolean checkIn(SemanticParserRequest request) {
        logger.info("SO-1-2.账户权限校验...");
        return true;
    }


    private void setAccountData(SemanticParserRequest request) {
        logger.info("SO-1-3.账户自定义参数设置...");
    }
}
