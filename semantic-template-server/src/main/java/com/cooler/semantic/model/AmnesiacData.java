package com.cooler.semantic.model;

public class AmnesiacData{
        /**
         * 当前上下文ID
         */
        private Integer currentContextId;

        /**
         * 历史上下文ID
         */
        private Integer historyContextId;

        /**
         * 上下文距离
         */
        private int contextDistance;

        /**
         * 失忆算法类型
         */
        private Integer amnesiacAlgorithmType;

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
    }
