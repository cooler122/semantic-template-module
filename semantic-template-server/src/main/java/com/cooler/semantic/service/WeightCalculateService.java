package com.cooler.semantic.service;

import com.cooler.semantic.model.SentenceInfo;
import com.cooler.semantic.model.WordInfo;

import java.util.List;

public interface WeightCalculateService {
    /**
     * 设置权重：为wordInfosFilterList设置权重
     * @param wordInfosFilterList
     */
    void calculateWeight(List<SentenceInfo> wordInfosFilterList);
}
