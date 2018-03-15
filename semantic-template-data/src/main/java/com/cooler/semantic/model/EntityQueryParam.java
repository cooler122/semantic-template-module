package com.cooler.semantic.model;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.constant.Constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityQueryParam implements Serializable{
    /**
     * 访问类型，分为5种（实体先分为两种：必须和非必须，下面0代表非必须；而1~4代表必须）：
     * 0，默认参数，这个参数不必须，不询问；
     * 1，问答题模式询问；
     * 2，选择题模式询问；
     * 3，先问接口，如果没有答案则问答题模式询问人；
     * 4，先问接口，如果没有答案则选择题模式询问人
     */
    private int queryType = Constant.AB_NO_QUERY_MODE;

    /**
     * 问人1：直接的问题（问答题型问题）
     */
    private String directQuestion = null;

    /**
     * 问人2：间接的问题（选择题型问题）
     */
    private String indirectQuestion = null;

    /**
     * 间接问题的实体问题集Map
     */
    private Map<String, String> indirectQueryParamMap = null;

    /**
     * 问接口3、4：请求接口型问题
     */
    private String queryUrl = null;

    /**
     * 请求类型post、get等
     */
    private String method = null;

    /**
     * 请求参数集
     */
    private Map<String, String> valueMap = null;

    public EntityQueryParam() {
    }

    public EntityQueryParam(int queryType) {
        this.queryType = queryType;
    }

    public EntityQueryParam(int queryType, String directQuestion) {
        this.queryType = queryType;
        this.directQuestion = directQuestion;
    }

    public EntityQueryParam(int queryType, String indirectQuestion, Map<String, String> indirectQueryParamMap) {
        this.queryType = queryType;
        this.indirectQuestion = indirectQuestion;
        this.indirectQueryParamMap = indirectQueryParamMap;
    }

    public EntityQueryParam(int queryType, String directQuestion, String queryUrl, String method, Map<String, String> valueMap) {
        this.queryType = queryType;
        this.directQuestion = directQuestion;
        this.queryUrl = queryUrl;
        this.method = method;
        this.valueMap = valueMap;
    }

    public EntityQueryParam(int queryType, String indirectQuestion, Map<String, String> indirectQueryParamMap, String queryUrl, String method, Map<String, String> valueMap) {
        this.queryType = queryType;
        this.indirectQuestion = indirectQuestion;
        this.indirectQueryParamMap = indirectQueryParamMap;
        this.queryUrl = queryUrl;
        this.method = method;
        this.valueMap = valueMap;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public String getDirectQuestion() {
        return directQuestion;
    }

    public void setDirectQuestion(String directQuestion) {
        this.directQuestion = directQuestion;
    }

    public String getIndirectQuestion() {
        return indirectQuestion;
    }

    public void setIndirectQuestion(String indirectQuestion) {
        this.indirectQuestion = indirectQuestion;
    }

    public Map<String, String> getIndirectQueryParamMap() {
        return indirectQueryParamMap;
    }

    public void setIndirectQueryParamMap(Map<String, String> indirectQueryParamMap) {
        this.indirectQueryParamMap = indirectQueryParamMap;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, String> valueMap) {
        this.valueMap = valueMap;
    }

    public static void main(String args[]){
        int queryType = Constant.AB_QUESTION_RESPONSE_MODE;

        String directQuestion = null;

        String indirectQuestion = "要打印凭条吗？";

        Map<String, String> indirectQuestionMap = new HashMap<>();
        indirectQuestionMap.put("是", "要");
        indirectQuestionMap.put("否", "不要");

        String queryUrl = "";

        String method = "";

        Map<String, String> valueMap = new HashMap<>();

        EntityQueryParam entityQueryParam0 = new EntityQueryParam(Constant.AB_NO_QUERY_MODE);
        EntityQueryParam entityQueryParam1 = new EntityQueryParam(Constant.AB_QUESTION_RESPONSE_MODE, directQuestion);
        EntityQueryParam entityQueryParam2 = new EntityQueryParam(Constant.AB_CHOICE_QUESTION_MODE, indirectQuestion, indirectQuestionMap);
        EntityQueryParam entityQueryParam3 = new EntityQueryParam(Constant.AB_INTERFACE_QUESTION_RESPONSE_MODE, directQuestion, queryUrl, method, valueMap);
        EntityQueryParam entityQueryParam4 = new EntityQueryParam(Constant.AB_INTERFACE_CHOICE_QUESTION_MODE, indirectQuestion, indirectQuestionMap, queryUrl, method, valueMap);

//        System.out.println(JSON.toJSONString(entityQueryParam0));
//        System.out.println(JSON.toJSONString(entityQueryParam1));
        System.out.println(JSON.toJSONString(entityQueryParam2));
//        System.out.println(JSON.toJSONString(entityQueryParam3));
//        System.out.println(JSON.toJSONString(entityQueryParam4));
    }

}
