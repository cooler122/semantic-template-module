package com.cooler.semantic.entity;

public class LogDataCalculation {
    private Integer id;

    private Integer processId;

    private Integer algorithmType;

    private String formula;

    private String formulaData;

    public LogDataCalculation(Integer id, Integer processId, Integer algorithmType, String formula, String formulaData) {
        this.id = id;
        this.processId = processId;
        this.algorithmType = algorithmType;
        this.formula = formula;
        this.formulaData = formulaData;
    }

    public LogDataCalculation() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public Integer getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(Integer algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula == null ? null : formula.trim();
    }

    public String getFormulaData() {
        return formulaData;
    }

    public void setFormulaData(String formulaData) {
        this.formulaData = formulaData == null ? null : formulaData.trim();
    }
}