package com.cooler.semantic.constant;


public class Constant{

    public static final int NO_STORE = 0;                                              //不存储
    public static final int STORE_LOCAL = 1;                                          //本地存储
    public static final int STORE_REMOTE = 2;                                         //远程存储
    public static final int STORE_LOCAL_REMOTE = 3;                                  //本地和远程都存储


    //------------------------------------------------------------------------------相似度选择标签
    public static final int JACCARD_VOLUME_RATE = 1;
    public static final int JACCARD_WEIGHT_RATE = 2;
    public static final int JACCARD_VOLUME_WEIGHT_RATE = 3;

    public static final int COSINE = 4;
    public static final int PEARSON = 5;






}
