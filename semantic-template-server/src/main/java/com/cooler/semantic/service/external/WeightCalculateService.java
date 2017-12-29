package com.cooler.semantic.service.external;

import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.model.SentenceVectorParam;

import java.util.List;

public interface WeightCalculateService {

    /**
     * 设置权重：为wordInfosFilterList设置权重，并转换
     * @param accountId 账号
     * @param sentenceVectorParams  句子向量
     * @return
     */
    List<SentenceVector> calculateWeight(Integer accountId, List<SentenceVectorParam> sentenceVectorParams);
}
