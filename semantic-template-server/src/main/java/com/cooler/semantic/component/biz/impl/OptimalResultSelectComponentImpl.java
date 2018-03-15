package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.internal.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component("optimalResultSelectComponent")
public class OptimalResultSelectComponentImpl extends FunctionComponentBase<SVRuleInfo, SVRuleInfo> {
    private static Logger logger = LoggerFactory.getLogger(OptimalResultSelectComponentImpl.class.getName());
    @Autowired
    private RuleService ruleService;

    public OptimalResultSelectComponentImpl() {
        super("ORSC", null, "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<SVRuleInfo> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("ORSC.最佳结果选择");
        Integer accountId = contextOwner.getCoreAccountId();
        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();
        double accuracyThreshold = request.getAccuracyThreshold();                                                     //用户设置的全局阈值

        String resultCode = null;
        DataComponent<SVRuleInfo> optimalSvRuleInfo_DataComponent = null;
        DataComponent<SVRuleInfo> dataComponent_LPM = componentConstant.getDataComponent("optimalSvRuleInfo_LPM", contextOwner);
        if(dataComponent_LPM != null && dataComponent_LPM.getData() != null){                                                                       //优先缺参结果LPM
            optimalSvRuleInfo_DataComponent = dataComponent_LPM;
            resultCode = "ORSC_S_L";
        }else{
            DataComponent<SVRuleInfo> dataComponent_CPM = componentConstant.getDataComponent("optimalSvRuleInfo_CPM", contextOwner);
            DataComponent<SVRuleInfo> dataComponent_FPM = componentConstant.getDataComponent("optimalSvRuleInfo_FPM", contextOwner);
            if((dataComponent_CPM == null || dataComponent_CPM.getData() == null) && (dataComponent_FPM != null && dataComponent_FPM.getData() != null)){ //如果换参结果为空，而全参结果不为空，则选全参结果
                optimalSvRuleInfo_DataComponent = dataComponent_FPM;
                double ruleAccuracyThresholdOccupyRate = getRuleAccuracyThresholdOccupyRate(optimalSvRuleInfo_DataComponent, accountId, accuracyThreshold);
                if(ruleAccuracyThresholdOccupyRate > 1.0d){                                                             //全参匹配值超过阈值，算匹配上
                    resultCode = "ORSC_S_F1";
                }else{                                                                                                  //全参匹配值没有超过阈值，不能算匹配上
                    resultCode = "ORSC_S_F2";
                }
            }else if((dataComponent_CPM != null && dataComponent_CPM.getData() != null) && (dataComponent_FPM == null || dataComponent_FPM.getData() == null)){   //如果换参结果不为空，而全参结果为空，则选换参结果
                optimalSvRuleInfo_DataComponent = dataComponent_CPM;
                double ruleAccuracyThresholdOccupyRate = getRuleAccuracyThresholdOccupyRate(optimalSvRuleInfo_DataComponent, accountId, accuracyThreshold);
                if(ruleAccuracyThresholdOccupyRate > 1.0d){                                                             //全参匹配值超过阈值，算匹配上
                    resultCode = "ORSC_S_C1";
                }else{                                                                                                  //全参匹配值没有超过阈值，不能算匹配上
                    resultCode = "ORSC_S_C2";
                }
            }else if((dataComponent_CPM != null && dataComponent_CPM.getData() != null) && (dataComponent_FPM != null && dataComponent_FPM.getData() != null)){   //如果两者都不为空，选相似度大的结果
                double ruleAccuracyThresholdOccupyRate_cpm = getRuleAccuracyThresholdOccupyRate(dataComponent_CPM, accountId, accuracyThreshold);
                double ruleAccuracyThresholdOccupyRate_fpm = getRuleAccuracyThresholdOccupyRate(dataComponent_FPM, accountId, accuracyThreshold);
                if(ruleAccuracyThresholdOccupyRate_cpm > ruleAccuracyThresholdOccupyRate_fpm){                          //这个时候比较大小没有意义了，不同规则有不同的阈值；是看谁超出阈值更多，才选择谁；如果都小于1，也要选占比比较大的那个。
                    optimalSvRuleInfo_DataComponent = dataComponent_CPM;
                    if(ruleAccuracyThresholdOccupyRate_cpm > 1.0d){                                                     //换参匹配值超过阈值，算匹配上
                        resultCode = "ORSC_S_C1";
                    }else{                                                                                             //换参匹配值没有超过阈值，不能算匹配上
                        resultCode = "ORSC_S_C2";
                    }
                }else{
                    optimalSvRuleInfo_DataComponent = dataComponent_FPM;
                    if(ruleAccuracyThresholdOccupyRate_fpm > 1.0d){                                                     //全参匹配值超过阈值，算匹配上
                        resultCode = "ORSC_S_F1";
                    }else{                                                                                             //全参匹配值没有超过阈值，不能算匹配上
                        resultCode = "ORSC_S_F2";
                    }
                }
            }else{                                                                                                      //如果两者都为空，谁都不选
                resultCode = "ORSC_S";
            }
        }
        if(optimalSvRuleInfo_DataComponent != null && optimalSvRuleInfo_DataComponent.getData() != null){
            SVRuleInfo optimalSvRuleInfo = optimalSvRuleInfo_DataComponent.getData();
            Integer sentenceVectorId = optimalSvRuleInfo.getSentenceVectorId();
            List<REntityWordInfo> matchedREntityWordInfos = optimalSvRuleInfo.getMatchedREntityWordInfos();
            for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                Double finalWeight = matchedREntityWordInfo.getWeights().get(sentenceVectorId);
                matchedREntityWordInfo.setWeights(Arrays.asList(finalWeight));                                          //实际上对于本轮对话，此处权重以没有作用，这里还是将最终确定用上的权重在这里设置，是为了下几轮对话，也许下几轮对话会用上。
            }
            optimalSvRuleInfo.setSentenceVectorId(0);                                                                   //上面将确定后的权重改变到了第0个位置，那么将此最佳SVRuleInfo的SentenceVectorId设置为0
            return new ComponentBizResult(resultCode, Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);  //此结果在本地和远程都要存储，无论它是否超过规则规定的阈值，都将记为历史记录
        }else{
            return new ComponentBizResult(resultCode);
        }
    }

    /**
     * 计算DataComponent的阈值占用率
     * @param dataComponent
     * @param accountId
     * @param accuracyThreshold
     * @return
     */
    private double getRuleAccuracyThresholdOccupyRate(DataComponent<SVRuleInfo> dataComponent, Integer accountId, double accuracyThreshold){
        SVRuleInfo svRuleInfo = dataComponent.getData();
        Double similarity = svRuleInfo.getSimilarity();                                                 //前面计算出的全参相似度
        Integer ruleId = svRuleInfo.getRuleId();
        Double ruleAccuracyThreshold = ruleService.selectAccuracyThresholdByIdAccountId(accountId, ruleId);
        ruleAccuracyThreshold = (ruleAccuracyThreshold == Constant.RULE_DEFAULT_PARAM) ? accuracyThreshold : ruleAccuracyThreshold;         //全参规则阈值
        svRuleInfo.setRunningAccuracyThreshold(ruleAccuracyThreshold);                                                  //获取的时候，立刻将此
        Double ruleAccuracyThresholdOccupyRate_fpm = similarity / ruleAccuracyThreshold;
        return ruleAccuracyThresholdOccupyRate_fpm;
    }
}
