package com.cooler.semantic.model;


import java.io.Serializable;

public class WordInfo implements Serializable {
    private int wordId;                        //来自数据库（数据库对应的wordId，Constant.Word_Id_Not_Exist，即-1为初始状态，如果查了数据库还为-1，表示数据库不存在此词语）
    private String word;                        //词语
    private double weight;                     // 权重

    public WordInfo() {
    }

    public WordInfo(int wordId, String word, double weight) {
        this.wordId = wordId;
        this.word = word;
        this.weight = weight;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}