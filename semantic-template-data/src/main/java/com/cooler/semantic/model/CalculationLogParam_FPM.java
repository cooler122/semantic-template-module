package com.cooler.semantic.model;

import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.console.SimilarityCalculationData_FPM;

import java.util.List;
import java.util.Map;

public class CalculationLogParam_FPM {

    /**
     * 1.句子向量集
     */
    private List<SentenceVector> sentenceVectors = null;

    /**
     * 2.匹配到的rule和实体数据集 ---> entityData1、entityData2、entityData3、entityData4...
     */
    private List<Map<String, List<RRuleEntity>>> hitRRuleEntityMaps = null;

    /**
     * 3.预选出的5个SVRuleInfo集合
     */
    private List<SVRuleInfo> svRuleInfosTopFive = null;

    /**
     * 4.各个ruleId下面的RRE数据
     */
    private Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap = null;

    /**
     * 5.相似度计算数据体
     */
    private SimilarityCalculationData_FPM similarityCalculationData_fpm = null;

    /**
     * 6.最优结果
     */
    private SVRuleInfo optimalSvRuleInfo_FPM = null;

    //------------------------------------------------------------------------------------------------------------------gets、sets


    public List<SentenceVector> getSentenceVectors() {
        return sentenceVectors;
    }

    public void setSentenceVectors(List<SentenceVector> sentenceVectors) {
        this.sentenceVectors = sentenceVectors;
    }

    public List<Map<String, List<RRuleEntity>>> getHitRRuleEntityMaps() {
        return hitRRuleEntityMaps;
    }

    public void setHitRRuleEntityMaps(List<Map<String, List<RRuleEntity>>> hitRRuleEntityMaps) {
        this.hitRRuleEntityMaps = hitRRuleEntityMaps;
    }

    public List<SVRuleInfo> getSvRuleInfosTopFive() {
        return svRuleInfosTopFive;
    }

    public void setSvRuleInfosTopFive(List<SVRuleInfo> svRuleInfosTopFive) {
        this.svRuleInfosTopFive = svRuleInfosTopFive;
    }

    public Map<Integer, Map<String, RRuleEntity>> getRuleId_RRuleEntityDataMap() {
        return ruleId_RRuleEntityDataMap;
    }

    public void setRuleId_RRuleEntityDataMap(Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap) {
        this.ruleId_RRuleEntityDataMap = ruleId_RRuleEntityDataMap;
    }

    public SimilarityCalculationData_FPM getSimilarityCalculationData_fpm() {
        return similarityCalculationData_fpm;
    }

    public void setSimilarityCalculationData_fpm(SimilarityCalculationData_FPM similarityCalculationData_fpm) {
        this.similarityCalculationData_fpm = similarityCalculationData_fpm;
    }

    public SVRuleInfo getOptimalSvRuleInfo_FPM() {
        return optimalSvRuleInfo_FPM;
    }

    public void setOptimalSvRuleInfo_FPM(SVRuleInfo optimalSvRuleInfo_FPM) {
        this.optimalSvRuleInfo_FPM = optimalSvRuleInfo_FPM;
    }
}
