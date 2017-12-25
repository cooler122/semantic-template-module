package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.service.external.PreprocessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("preprocessingService")
public class PreprocessingServiceImpl implements PreprocessingService {

    private static Logger logger = LoggerFactory.getLogger(PreprocessingServiceImpl.class.getName());

    @Override
    public boolean preprocess(SemanticParserRequest request) {
        if(!validate(request))  return false;
        if(!checkIn(request))   return false;
        setAccountData(request);
        return true;
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
