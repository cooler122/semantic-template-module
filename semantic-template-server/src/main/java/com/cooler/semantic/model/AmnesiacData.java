package com.cooler.semantic.model;

public class AmnesiacData{
        /**
         * 1.当前上下文ID
         */
        private Integer currentContextId;

        /**
         * 2.历史上下文ID
         */
        private Integer historyContextId;

        /**
         * 3.上下文距离
         */
        private int contextDistance;

        /**
         * 4.失忆算法类型
         */
        private Integer amnesiacAlgorithmType;

        /**
         * 5.底数
         */
        private double baseNumber;

        /**
         * 6.失忆系数
         */
        private double coefficient;

    //------------------------------------------------------------------------------------------------------------------gets、sets

        public Integer getCurrentContextId() {
            return currentContextId;
        }

        public void setCurrentContextId(Integer currentContextId) {
            this.currentContextId = currentContextId;
        }

        public Integer getHistoryContextId() {
            return historyContextId;
        }

        public void setHistoryContextId(Integer historyContextId) {
            this.historyContextId = historyContextId;
        }

        public int getContextDistance() {
            return contextDistance;
        }

        public void setContextDistance(int contextDistance) {
            this.contextDistance = contextDistance;
        }

        public Integer getAmnesiacAlgorithmType() {
            return amnesiacAlgorithmType;
        }

        public void setAmnesiacAlgorithmType(Integer amnesiacAlgorithmType) {
            this.amnesiacAlgorithmType = amnesiacAlgorithmType;
        }

        public double getBaseNumber() {
            return baseNumber;
        }

        public void setBaseNumber(double baseNumber) {
            this.baseNumber = baseNumber;
        }

        public double getCoefficient() {
            return coefficient;
        }

        public void setCoefficient(double coefficient) {
            this.coefficient = coefficient;
        }
}
