package com.cooler.semantic.model.console;

import com.cooler.semantic.model.REntityWordInfo;

import java.util.List;

public class SimilarityCalculationData_LPM {

        /**
         * 1.句子向量ID
         */
        private Integer sentenceVectorId;

        /**
         * 2.上下文ID
         */
        private Integer contextId;

        /**
         * 3.句子向量实体
         */
        private List<List<REntityWordInfo>> svInputREWIs;

        /**
         * 4.规则向量实体
         */
        private List<REntityWordInfo> ruleInputREWIs;

        /**
         * 5.算法类型
         */
        private Integer algorithmType;

        /**
         * 6.算法公式
         */
        private String algorithmFormula;

        /**
         * 7.交集实体的句子向量端数量占比和规则端数量占比（此量根据算法而不同，这是贾卡德算法产生的值，这里放两个值，用字符串合起来，形式如：( entityId - entityName ) : svVolumnRate - ruleVolumnRate）
         */
        private List<String> intersectionEntityVolumnRates;

        /**
         * 8.规则端，交集实体的数量占比和权重占比（此量根据算法而不同，这是贾卡德算法产生的值，这里放两个值，用字符串合起来，形式如：( entityId - entityName ) : svWeightRate - ruleWeightRate）
         */
        private List<String> intersectionEntityWeightRates;

        /**
         * 9.相似度分值以及其细节值(形如：similarityValue -> intersectionVolumeRateOccupancy * intersectionWeightOccupancy
         */
        private String similarityValue;

        //--------------------------------------------------------------------------------------------------------------gets、sets


        public Integer getSentenceVectorId() {
            return sentenceVectorId;
        }

        public void setSentenceVectorId(Integer sentenceVectorId) {
            this.sentenceVectorId = sentenceVectorId;
        }

        public Integer getContextId() {
            return contextId;
        }

        public void setContextId(Integer contextId) {
            this.contextId = contextId;
        }

        public List<List<REntityWordInfo>> getSvInputREWIs() {
            return svInputREWIs;
        }

        public void setSvInputREWIs(List<List<REntityWordInfo>> svInputREWIs) {
            this.svInputREWIs = svInputREWIs;
        }

        public List<REntityWordInfo> getRuleInputREWIs() {
            return ruleInputREWIs;
        }

        public void setRuleInputREWIs(List<REntityWordInfo> ruleInputREWIs) {
            this.ruleInputREWIs = ruleInputREWIs;
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

        public List<String> getIntersectionEntityVolumnRates() {
            return intersectionEntityVolumnRates;
        }

        public void setIntersectionEntityVolumnRates(List<String> intersectionEntityVolumnRates) {
            this.intersectionEntityVolumnRates = intersectionEntityVolumnRates;
        }

        public List<String> getIntersectionEntityWeightRates() {
            return intersectionEntityWeightRates;
        }

        public void setIntersectionEntityWeightRates(List<String> intersectionEntityWeightRates) {
            this.intersectionEntityWeightRates = intersectionEntityWeightRates;
        }

        public String getSimilarityValue() {
                return similarityValue;
            }

        public void setSimilarityValue(String similarityValue) {
            this.similarityValue = similarityValue;
        }
}