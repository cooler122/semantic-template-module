package com.cooler.semantic.model;

public class ChangeParamMatchRateInfo {
    /**
     * 原句端值：确定此匹配体的Key（包含sentenceVectorId、entityType、entityId）
     */
    private CoordinateKey coordinateKey;

    /**
     * 历史端值：历史记录里面碰到的REWI
     */
    private REntityWordInfo historyREntityWordInfo;

    /**
     * 原句端个数占比
     */
    private double svVolumnOccupyRate = 0d;

    /**
     * 原句端权重占比
     */
    private double svWeightOccupyRate = 0d;

    /**
     * 历史端个数占比
     */
    private double historyVolumnOccupyRate = 0d;

    /**
     * 历史端权重占比
     */
    private double historyWeightOccupyRate = 0d;

    public CoordinateKey getCoordinateKey() {
        return coordinateKey;
    }

    public void setCoordinateKey(CoordinateKey coordinateKey) {
        this.coordinateKey = coordinateKey;
    }

    public REntityWordInfo getHistoryREntityWordInfo() {
        return historyREntityWordInfo;
    }

    public void setHistoryREntityWordInfo(REntityWordInfo historyREntityWordInfo) {
        this.historyREntityWordInfo = historyREntityWordInfo;
    }

    public double getSvVolumnOccupyRate() {
        return svVolumnOccupyRate;
    }

    public void setSvVolumnOccupyRate(double svVolumnOccupyRate) {
        this.svVolumnOccupyRate = svVolumnOccupyRate;
    }

    public double getSvWeightOccupyRate() {
        return svWeightOccupyRate;
    }

    public void setSvWeightOccupyRate(double svWeightOccupyRate) {
        this.svWeightOccupyRate = svWeightOccupyRate;
    }

    public double getHistoryVolumnOccupyRate() {
        return historyVolumnOccupyRate;
    }

    public void setHistoryVolumnOccupyRate(double historyVolumnOccupyRate) {
        this.historyVolumnOccupyRate = historyVolumnOccupyRate;
    }

    public double getHistoryWeightOccupyRate() {
        return historyWeightOccupyRate;
    }

    public void setHistoryWeightOccupyRate(double historyWeightOccupyRate) {
        this.historyWeightOccupyRate = historyWeightOccupyRate;
    }
}
