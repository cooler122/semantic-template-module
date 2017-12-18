package com.cooler.semantic.api;

import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;

public interface SemanticParserFacade {
    /**
     * 语义解析接口
     * @param semanticParserRequest 语义解析请求体
     * @return  语义解析回应体
     */
    SemanticParserResponse semanticParse(SemanticParserRequest semanticParserRequest);
}
