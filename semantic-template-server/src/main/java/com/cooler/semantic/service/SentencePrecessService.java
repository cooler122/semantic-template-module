package com.cooler.semantic.service;

import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.SentenceInfo;

import java.util.List;

public interface SentencePrecessService {

    /**
     * 句子处理：分词，设置权重，转存，由于后续有可能分词方式不只一种，所以返回一个分词段List，但是初始版本里面先只返回一个size=1的List
     * @param request   请求体
     * @return  处理好的SentenceInfo对象
     */
    List<SentenceInfo> sentenceProcess(SemanticParserRequest request);
}
