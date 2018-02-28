package com.cooler.semantic.model.console;

import com.cooler.semantic.model.REntityWordInfo;

import java.util.List;

public class CoupleAlterationRateData{
        /**
         * 1a.本轮分词ID
         */
        private Integer sentenceVectorId = null;

        /**
         * 1b.历史上下文ID
         */
        private Integer contextId = null;

        /**
         * 2.合并的实体参数集
         */
        private List<REntityWordInfo> combinedREntityWordInfos = null;

        /**
         * 3.SV端权重占比
         */
        private double svWeightOccupyRate = 0.0d;

        /**
         * 4.Rule端权重占比
         */
        private double ruleWeightOccupyRate = 0.0d;

        /**
         * 5a.合并的实体数量阈值
         */
        private int combinedEntityCountAccuracyThreshold = 0;

        /**
         * 5b.占SV端的权重阈值
         */
        private double weightRateAccuracyThreshold4SV = 0.0d;

        /**
         * 5c.占Rule端的权重阈值
         */
        private double weightRateAccuracyThreshold4Rule = 0.0d;

        /**
         * 6.是否跨越门槛
         */
        private boolean isCrossed = false;

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

        public List<REntityWordInfo> getCombinedREntityWordInfos() {
            return combinedREntityWordInfos;
        }

        public void setCombinedREntityWordInfos(List<REntityWordInfo> combinedREntityWordInfos) {
            this.combinedREntityWordInfos = combinedREntityWordInfos;
        }

        public double getSvWeightOccupyRate() {
                    return svWeightOccupyRate;
                }

        public void setSvWeightOccupyRate(double svWeightOccupyRate) {
            this.svWeightOccupyRate = svWeightOccupyRate;
        }

        public double getRuleWeightOccupyRate() {
            return ruleWeightOccupyRate;
        }

        public void setRuleWeightOccupyRate(double ruleWeightOccupyRate) {
            this.ruleWeightOccupyRate = ruleWeightOccupyRate;
        }

        public int getCombinedEntityCountAccuracyThreshold() {
            return combinedEntityCountAccuracyThreshold;
        }

        public void setCombinedEntityCountAccuracyThreshold(int combinedEntityCountAccuracyThreshold) {
            this.combinedEntityCountAccuracyThreshold = combinedEntityCountAccuracyThreshold;
        }

        public double getWeightRateAccuracyThreshold4SV() {
            return weightRateAccuracyThreshold4SV;
        }

        public void setWeightRateAccuracyThreshold4SV(double weightRateAccuracyThreshold4SV) {
            this.weightRateAccuracyThreshold4SV = weightRateAccuracyThreshold4SV;
        }

        public double getWeightRateAccuracyThreshold4Rule() {
            return weightRateAccuracyThreshold4Rule;
        }

        public void setWeightRateAccuracyThreshold4Rule(double weightRateAccuracyThreshold4Rule) {
            this.weightRateAccuracyThreshold4Rule = weightRateAccuracyThreshold4Rule;
        }

        public boolean isCrossed() {
            return isCrossed;
        }

        public void setCrossed(boolean crossed) {
            isCrossed = crossed;
        }
    }