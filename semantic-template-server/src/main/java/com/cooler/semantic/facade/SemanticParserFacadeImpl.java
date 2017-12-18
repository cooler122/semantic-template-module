package com.cooler.semantic.facade;

import com.cooler.semantic.api.SemanticParserFacade;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("semanticParserFacade")
public class SemanticParserFacadeImpl implements SemanticParserFacade {

    private static Logger logger = LoggerFactory.getLogger(SemanticParserFacadeImpl.class.getName());

    public SemanticParserResponse semanticParse(SemanticParserRequest semanticParserRequest) {
        logger.info("semanticParse start....");
        System.out.println("semanticParse 开始...");
        return null;
    }
}
