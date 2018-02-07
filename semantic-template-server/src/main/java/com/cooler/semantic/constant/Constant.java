package com.cooler.semantic.constant;


public class Constant{

    //------------------------------------------------------------------------------匹配类型
    public static final int NO_MATCH = 0;                                               //没匹配
    public static final int LPM = 1;                                                    //缺参匹配
    public static final int CPM = 2;                                                    //换参匹配
    public static final int FPM = 3;                                                    //全参匹配
    public static final int OPTIMAL = 4;                                               //最终选择的匹配


    //------------------------------------------------------------------------------组件类型
    public static final int FUNCTION_COMPONENT = 1;                                   //功能组件
    public static final int VERDICT_COMPONENT = 2;                                    //分支组件

    //------------------------------------------------------------------------------实体类型
    public static final int WORD_ENTITY = 0;                                           //词语实体
    public static final int STRINGS_ENTIRY = 1;                                       //字符串集实体
    public static final int ANAPHORA_ENTIRY = 2;                                      //指代实体

    //------------------------------------------------------------------------------存储类型
    public static final int NO_STORE = 0;                                              //不存储
    public static final int STORE_LOCAL = 1;                                          //本地存储
    public static final int STORE_REMOTE = 2;                                         //远程存储
    public static final int STORE_LOCAL_REMOTE = 3;                                  //本地和远程都存储

    //------------------------------------------------------------------------------相似度算法类型
    public static final int JACCARD_VOLUME_RATE = 1;                                //贾卡德（只考虑实体占用率）
    public static final int JACCARD_WEIGHT_RATE = 2;                                //贾卡德（只考虑权重占用率）
    public static final int JACCARD_VOLUME_WEIGHT_RATE = 3;                        //贾卡德（只考虑实体、权重占用率）
    public static final int COSINE = 4;                                               //余弦
    public static final int PEARSON = 5;                                              //皮尔逊

    //------------------------------------------------------------------------------解析结果类型
    public static final int RUNNING_RESULT = 1;                                     //运行中意图结果
    public static final int NO_RUNNING_RESULT = 2;                                  //非运行中意图结果
    public static final int MISSING_RESULT = 3;                                     //缺参结果
    public static final int FAIL_RESULT = 4;                                        //失败结果

    //------------------------------------------------------------------------------拥有历史数据的组件类型
    public static final String[] DATA_COMPONENT_IDs = {
            "historyDataComponents",
            "semanticParserRequest",
            "sentenceVectors",
            "optimalSvRuleInfo_LPM",
            "optimalSvRuleInfo_CPM",
            "optimalSvRuleInfo_FPM",
            "createdReferSvRuleInfo",
            "sugguestData",
            "optimalSvRuleInfo",
            "semanticParserResponse"
    };

    //------------------------------------------------------------------------------日志类型
    public static final int NO_LOG = 0;                                              //无日志
    public static final int TEXT_LOG = 1;                                           //文本日志数据
    public static final int HTML_LOG = 2;                                           //静态HTML日志数据
    public static final int DATA_BASE_LOG = 3;                                      //数据库日志数据

    public static final double RULE_DEFAULT_PARAM = -1;                           //规定规则自定义阈值如果没有设置，其值为-1

    //------------------------------------------------------------------------------机器反问(Ask Back)模式类型
    public static final int AB_NO_QUERY_MODE = 0;                                      //不问模式
    public static final int AB_QUESTION_RESPONSE_MODE = 1;                            //问答题模式
    public static final int AB_CHOICE_QUESTION_MODE = 2;                               //选择题模式
    public static final int AB_INTERFACE_QUESTION_RESPONSE_MODE = 3;                 //先问接口，若失败则问答题模式
    public static final int AB_INTERFACE_CHOICE_QUESTION_MODE = 4;                   //先问接口，若失败则选择题模式


}
