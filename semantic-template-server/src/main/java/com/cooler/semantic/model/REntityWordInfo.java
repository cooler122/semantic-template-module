package com.cooler.semantic.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by zhangsheng on 2017/12/31.
 */
public class REntityWordInfo implements Serializable{

    private Logger logger = LoggerFactory.getLogger(REntityWordInfo.class);

    /**
     * 词语ID
     */
    private Integer wordId;

    /**
     * 词语
     */
    private String word;

    /**
     * 句子端的设置权重（针对于每一个分词模式下，每一个此REW的权重）
     */
    private Map<Integer, Double> weightMap = new HashMap<>();

    /**
     * 词性
     */
    private String nature;

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

    /**
     * 上下文编号（用此来标识，此RWE是当前本轮产生的还是历史对话产生的）
     */
    private Integer contextId;


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

    public Map<Integer, Double> getWeightMap() {
        return weightMap;
    }

    public void setWeightMap(Map<Integer, Double> weightMap) {
        this.weightMap = weightMap;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
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

    public Integer getContextId() {
        return contextId;
    }

    public void setContextId(Integer contextId) {
        this.contextId = contextId;
    }

    public REntityWordInfo clone(){
        REntityWordInfo rEntityWordInfo = null;
        try{
            rEntityWordInfo = (REntityWordInfo)super.clone();
        } catch(CloneNotSupportedException e) {
            logger.error(e.toString());
        }
        return rEntityWordInfo;
    }
}
