package com.cooler.semantic.service;

import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.SentenceInfo;

import java.util.List;

public interface EntityService {
    /**
     * 按照request中的参数，进行实体归属，并将归属的实体保存到sentenceInfo中
     * @param request   请求对象
     * @param sentenceInfos 句子信息对象
     * @return  处理后的句子对象集
     */
    List<SentenceInfo> entitySearch(SemanticParserRequest request, List<SentenceInfo> sentenceInfos);
}
