package com.cooler.semantic.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculationLogParam_CPM {

    //------------------------------------------------------------------------------------------------------------------1.历史数据
    /**
     * 1.1.历史结果数据
     */
    private Map<String, SVRuleInfo> contextId_svRuleInfoMap = new HashMap<>();

    /**
     * 1.2.历史REWIMap
     */
    Map<String, List<REntityWordInfo>> historyREWIMap = new HashMap<>();

    //------------------------------------------------------------------------------------------------------------------2.运行数据
    /**
     * 2.1.关联到了的数据Map<{sentenceVectorId, currentEntityType, currentEntityId}, hitCurrentREWI>
     */
    Map<String, REntityWordInfo> hitCurrentREntityWordInfoMap = new HashMap<>();

    /**
     * 2.2.统计值Map<sentenceVectorId_contextId, 统计数据值>
     */
    Map<String, Double> svIdcontextId_productValueMap = new HashMap<>();

    /**
     * 2.3.最高匹配值的sentenceVectorId（和下面的contextId绑定）
     */
    Integer maxValueSentenceVectorId = null;

    /**
     * 2.4.最高匹配值的ContextId
     */
    Integer maxValueContextId = null;

    /**
     * 2.5.最高匹配值（此值是匹配上的实体的(1d / currentSVSize * currentWeight) * 3 + (volumeIncrement * historyWeight)的和值）
     */
    Double maxValue = 0d;

    //------------------------------------------------------------------------------------------------------------------3.结果数据
    /**
     * 3.换参匹配产生的最优SVRuleInfo
     */
    SVRuleInfo changeParamOptimalSvRuleInfo = null;

    //------------------------------------------------------------------------------------------------------------------4.评判数据
    /**
     * 4.1.历史规则里的实体数量
     */
    int historyEntitiesCount = 0;

    /**
     * 4.2.当前句子向量里的实体数量
     */
    int currentEntitiesCount = 0;

    /**
     * 4.3.当前核心实体数
     */
    int currentCoreEntitiesCount = 0;

    /**
     * 4.4.可换参数所占的权重在句子向量中权重的占比
     */
    double currentHitEntityWeightRate = 0d;

    public Map<String, SVRuleInfo> getContextId_svRuleInfoMap() {
        return contextId_svRuleInfoMap;
    }

    public void setContextId_svRuleInfoMap(Map<String, SVRuleInfo> contextId_svRuleInfoMap) {
        this.contextId_svRuleInfoMap = contextId_svRuleInfoMap;
    }

    public Map<String, List<REntityWordInfo>> getHistoryREWIMap() {
        return historyREWIMap;
    }

    public void setHistoryREWIMap(Map<String, List<REntityWordInfo>> historyREWIMap) {
        this.historyREWIMap = historyREWIMap;
    }

    public Map<String, REntityWordInfo> getHitCurrentREntityWordInfoMap() {
        return hitCurrentREntityWordInfoMap;
    }

    public void setHitCurrentREntityWordInfoMap(Map<String, REntityWordInfo> hitCurrentREntityWordInfoMap) {
        this.hitCurrentREntityWordInfoMap = hitCurrentREntityWordInfoMap;
    }

    public Map<String, Double> getSvIdcontextId_productValueMap() {
        return svIdcontextId_productValueMap;
    }

    public void setSvIdcontextId_productValueMap(Map<String, Double> svIdcontextId_productValueMap) {
        this.svIdcontextId_productValueMap = svIdcontextId_productValueMap;
    }

    public Integer getMaxValueSentenceVectorId() {
        return maxValueSentenceVectorId;
    }

    public void setMaxValueSentenceVectorId(Integer maxValueSentenceVectorId) {
        this.maxValueSentenceVectorId = maxValueSentenceVectorId;
    }

    public Integer getMaxValueContextId() {
        return maxValueContextId;
    }

    public void setMaxValueContextId(Integer maxValueContextId) {
        this.maxValueContextId = maxValueContextId;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public SVRuleInfo getChangeParamOptimalSvRuleInfo() {
        return changeParamOptimalSvRuleInfo;
    }

    public void setChangeParamOptimalSvRuleInfo(SVRuleInfo changeParamOptimalSvRuleInfo) {
        this.changeParamOptimalSvRuleInfo = changeParamOptimalSvRuleInfo;
    }

    public int getHistoryEntitiesCount() {
        return historyEntitiesCount;
    }

    public void setHistoryEntitiesCount(int historyEntitiesCount) {
        this.historyEntitiesCount = historyEntitiesCount;
    }

    public int getCurrentEntitiesCount() {
        return currentEntitiesCount;
    }

    public void setCurrentEntitiesCount(int currentEntitiesCount) {
        this.currentEntitiesCount = currentEntitiesCount;
    }

    public int getCurrentCoreEntitiesCount() {
        return currentCoreEntitiesCount;
    }

    public void setCurrentCoreEntitiesCount(int currentCoreEntitiesCount) {
        this.currentCoreEntitiesCount = currentCoreEntitiesCount;
    }

    public double getCurrentHitEntityWeightRate() {
        return currentHitEntityWeightRate;
    }

    public void setCurrentHitEntityWeightRate(double currentHitEntityWeightRate) {
        this.currentHitEntityWeightRate = currentHitEntityWeightRate;
    }
}
