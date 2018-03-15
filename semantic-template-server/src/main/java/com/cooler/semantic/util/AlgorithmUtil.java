package com.cooler.semantic.util;

import java.util.List;

public class AlgorithmUtil {
    /**
     * pearson算法
     * @param ruleWeightArray
     * @param sentenceVectorWeightArray
     * @return 相似度值
     */
    public static double pearson(List<Double> ruleWeightArray, List<Double> sentenceVectorWeightArray){
        double sumX = 0.0;
        double sumY = 0.0;
        double sumX_Sq = 0.0;
        double sumY_Sq = 0.0;
        double sumXY = 0.0;
        int size = ruleWeightArray.size();

        for (int i = 0; i < size; i ++) {
            double p1 = ruleWeightArray.get(i);
            double p2 = sentenceVectorWeightArray.get(i);
            sumX += p1;
            sumY += p2;
            sumX_Sq += Math.pow(p1, 2);
            sumY_Sq += Math.pow(p2, 2);
            sumXY += p1 * p2;
        }
        double numerator = sumXY - sumX * sumY / size;
        double denominator = Math.sqrt((sumX_Sq - sumX * sumX / size) * (sumY_Sq - sumY * sumY / size));

        if (denominator == 0) {                                                                                         // 分母不能为0
            return 0;
        }
        return numerator / denominator;
    }

}
