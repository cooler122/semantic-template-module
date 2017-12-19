package com.cooler.semantic.model;

import com.flw.semantic.entity.Term;

import java.util.List;

public class WordInfo extends Term {
    private int wordId;                        //来自数据库（数据库对应的wordId，Constant.Word_Id_Not_Exist，即-1为初始状态，如果查了数据库还为-1，表示数据库不存在此词语）
    private double weight;                                                  // 权重
    private boolean isInitial;                                             // 是否已经初始化
    private boolean isCustomizationWord;                                 //是否自定义分词
    private List<Integer> customizationIds;                                //如果是自定义分词，则其对应的accountId、sceneId、intentId、ruleId存放的列表
    private int flag;                                                       // 标记（是否需要过滤）
    private String nature;                                                  //词性

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }

    public boolean isCustomizationWord() {
        return isCustomizationWord;
    }

    public void setCustomizationWord(boolean customizationWord) {
        isCustomizationWord = customizationWord;
    }

    public List<Integer> getCustomizationIds() {
        return customizationIds;
    }

    public void setCustomizationIds(List<Integer> customizationIds) {
        this.customizationIds = customizationIds;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }
}