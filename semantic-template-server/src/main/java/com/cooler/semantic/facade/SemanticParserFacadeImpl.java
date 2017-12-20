package com.cooler.semantic.facade;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.api.SemanticParserFacade;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.EntityService;
import com.cooler.semantic.service.PreprocessingService;
import com.cooler.semantic.service.SentencePrecessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("semanticParserFacade")
public class SemanticParserFacadeImpl implements SemanticParserFacade {

    private static Logger logger = LoggerFactory.getLogger(SemanticParserFacadeImpl.class.getName());

    @Autowired
    private PreprocessingService preprocessingService;      //预处理组件（校验、查账户、设置）
    @Autowired
    private SentencePrecessService sentencePrecessService;         //句子处理组件（分词、权重、词频）
    @Autowired
    private EntityService entityService;                         //实体处理组件


    public SemanticParserResponse semanticParse(SemanticParserRequest request) {







        return null;
    }
}
