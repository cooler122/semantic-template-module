package com.cooler.semantic.service.external;

import com.cooler.semantic.model.SentenceVector;

import java.util.List;

public interface WeightCalculateService {
    /**
     * 设置权重：为wordInfosFilterList设置权重
     * @param wordInfosFilterList
     */
    void calculateWeight(List<SentenceVector> wordInfosFilterList);
}
