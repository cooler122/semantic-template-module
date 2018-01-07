package com.cooler.semantic.model;

/**
 * Created by zhangsheng on 2017/12/31.
 */
public class REntityWordInfo {

    /**
     * 词语ID
     */
    private Integer wordId;

    /**
     * 词语
     */
    private String word;

    /**
     * 实体ID
     */
    private Integer entityId;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 归一化词语
     */
    private String normalWord;

    /**
     * 实体类型
     */
    private Integer entityType;

    /**
     * 实体类型和实体ID的合并值，用以后续查询
     */
    private String entityTypeId;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getNormalWord() {
        return normalWord;
    }

    public void setNormalWord(String normalWord) {
        this.normalWord = normalWord;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getEntityTypeId() {
        return entityType == 0 ? entityType + "_" + wordId : entityType + "_" + entityId;           //如果等于0，标识常量实体，如果不等于0，是其它类别实体
    }

    public void setEntityTypeId(String entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

}
