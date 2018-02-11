package com.cooler.semantic.model;

import com.cooler.semantic.entity.Entity;

import java.util.List;

public class SimilarityCalculationData{
        /**
         * 句子向量实体
         */
        private List<Entity> svInputEntityParams;
        /**
         * 规则向量实体
         */
        private List<Entity> ruleInputEntityParams;
        /**
         * 算法类型
         */
        private Integer algorithmType;
        /**
         * 算法公式
         */
        private String algorithmFormula;
        /**
         * 交集实体的数量占比和数量占比（此量根据算法而不同，这是贾卡德算法产生的值，这里放两个值，用字符串合起来，形式如：( entityId - entityName ) : svVolumnRate - ruleVolumnRate）
         */
        private List<String> svIntersectionEntityVolumnRate;
        /**
         * 交集实体的数量占比和权重占比（此量根据算法而不同，这是贾卡德算法产生的值，这里放两个值，用字符串合起来，形式如：( entityId - entityName ) : svWeightRate - ruleWeightRate）
         */
        private List<String> ruleIntersectionEntityWeightRate;
        /**
         * 相似度分值以及其细节值(形如：similarityValue -> intersectionVolumeRateOccupancy * intersectionWeightOccupancy
         */
        private String aboutSimilarityValue;

        public List<Entity> getSvInputEntityParams() {
            return svInputEntityParams;
        }

        public void setSvInputEntityParams(List<Entity> svInputEntityParams) {
            this.svInputEntityParams = svInputEntityParams;
        }

        public List<Entity> getRuleInputEntityParams() {
            return ruleInputEntityParams;
        }

        public void setRuleInputEntityParams(List<Entity> ruleInputEntityParams) {
            this.ruleInputEntityParams = ruleInputEntityParams;
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

        public List<String> getSvIntersectionEntityVolumnRate() {
            return svIntersectionEntityVolumnRate;
        }

        public void setSvIntersectionEntityVolumnRate(List<String> svIntersectionEntityVolumnRate) {
            this.svIntersectionEntityVolumnRate = svIntersectionEntityVolumnRate;
        }

        public List<String> getRuleIntersectionEntityWeightRate() {
            return ruleIntersectionEntityWeightRate;
        }

        public void setRuleIntersectionEntityWeightRate(List<String> ruleIntersectionEntityWeightRate) {
            this.ruleIntersectionEntityWeightRate = ruleIntersectionEntityWeightRate;
        }

        public String getAboutSimilarityValue() {
            return aboutSimilarityValue;
        }

        public void setAboutSimilarityValue(String aboutSimilarityValue) {
            this.aboutSimilarityValue = aboutSimilarityValue;
        }
    }