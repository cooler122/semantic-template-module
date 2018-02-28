package com.cooler.semantic.model.console;

import java.util.List;
import java.util.Map;

public class SimilarityCalculationData_FPM {

    /**
     * 1.上下文ID
     */
    private Integer contextId;

    /**
     * 2.算法类型
     */
    private Integer algorithmType;

    /**
     * 3.算法公式
     */
    private String algorithmFormula;

    /**
     * 4.细节实体数据：Map<(svId_ruleId), (wordId_word_entityId_entityName_weight1, wordId_word_entityId_entityName_weight2, wordId_word_entityId_entityName_weight3...)>
     */
    Map<String, List<String>> ids_rewDatasMap = null;

    /**
     * 5.细节计算值数据：Map<(svId_ruleId), (similarityScore = intersectionVolumeRateOccupancy * intersectionWeightOccupancy) >
     */
    Map<String, String> ids_scoreMap = null;

    public Integer getContextId() {
        return contextId;
    }

    public void setContextId(Integer contextId) {
        this.contextId = contextId;
    }

    public Integer getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(Integer algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getAlgorithmFormula() {
        return algorithmFormula;
    }

    public void setAlgorithmFormula(String algorithmFormula) {
        this.algorithmFormula = algorithmFormula;
    }

    public Map<String, List<String>> getIds_rewDatasMap() {
        return ids_rewDatasMap;
    }

    public void setIds_rewDatasMap(Map<String, List<String>> ids_rewDatasMap) {
        this.ids_rewDatasMap = ids_rewDatasMap;
    }

    public Map<String, String> getIds_scoreMap() {
        return ids_scoreMap;
    }

    public void setIds_scoreMap(Map<String, String> ids_scoreMap) {
        this.ids_scoreMap = ids_scoreMap;
    }
}
