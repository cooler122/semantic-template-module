package com.cooler.semantic.service.external;

import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SentenceVector;

import java.util.List;

public interface AnaphoraResolutionService {

    /**
     * 指代消解
     * @param contextOwner      分此账户下的参数
     * @param sentenceVectors  句子向量
     * @return  指代消解后的新句子向量
     */
    List<SentenceVector> anaphoraResolution(ContextOwner contextOwner, List<SentenceVector> sentenceVectors);
}
