package com.cooler.semantic.constant;


public class Constant{

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
    public static final int JACCARD_VOLUME_RATE = 1;
    public static final int JACCARD_WEIGHT_RATE = 2;
    public static final int JACCARD_VOLUME_WEIGHT_RATE = 3;

    public static final int COSINE = 4;
    public static final int PEARSON = 5;






}
