package com.cooler.semantic.service.external;

import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.SentenceVector;

import java.util.List;

public interface EntitySearchService {
    /**
     * 按照request中的参数，进行实体归属，并将归属的实体保存到sentenceInfo中
     * @param request   请求对象
     * @param sentenceVectors 句子信息对象
     * @return  处理后的句子对象集
     */
    List<SentenceVector> entitySearch(SemanticParserRequest request, List<SentenceVector> sentenceVectors);
}
