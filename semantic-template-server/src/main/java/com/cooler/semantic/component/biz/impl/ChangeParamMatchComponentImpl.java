package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.model.*;
import com.cooler.semantic.service.external.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component("changeParamMatchComponent")
public class ChangeParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(ChangeParamMatchComponentImpl.class.getName());
    @Autowired
    private RedisService<SVRuleInfo> redisService;

    public ChangeParamMatchComponentImpl() {
        super("CPMC", "SO-6 ~ SO-7", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("changeParamMatch.换参匹配");

        //1.准备好5轮的历史数据
        Map<String, List<REntityWordInfo>> historyREWIMap = new HashMap<>();                                            //将每一轮的REWI放入Map<entityTypeId, List<REWI>>
        Map<Integer, Double> historyVolumeIncrementMap = new HashMap<>();                                               //记录每一轮历史对话的匹配上的REWI集合的各个权重的Map<contextId, 1/REWIs' size>
        for(int i = 0; i < 5; i ++){                                                                                   //先查询出5轮历史数据（实际上可能2轮就足够了）
            String lastI_OwnerIndex = contextOwner.getLastNOwnerIndex(i);
            Integer lastNContextId = contextOwner.getLastNContextId(i);
            DataComponentBase<SVRuleInfo> historyData = redisService.getCacheObject(lastI_OwnerIndex + "_" + "optimalSvRuleInfo");//TODO：以后看看能否5次放到一起查出来
            if(historyData != null){
                SVRuleInfo svRuleInfo = historyData.getData();
                List<REntityWordInfo> matchedREntityWordInfos = svRuleInfo.getMatchedREntityWordInfos();                //取出已经匹配过的历史REWI集合记录

                //TODO:这里不知道是不是该让缺参状态的上下文排除在外，当前先不排除缺参上下文吧，后续看效果
                for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                    String entityTypeId = matchedREntityWordInfo.getEntityTypeId();
                    List<REntityWordInfo> rEntityWordInfos = historyREWIMap.get(entityTypeId);
                    if(rEntityWordInfos == null){
                        rEntityWordInfos = new ArrayList<>();
                    }
                    rEntityWordInfos.add(matchedREntityWordInfo);
                    historyREWIMap.put(entityTypeId, rEntityWordInfos);
                }

                int historyMatchedREWISize = matchedREntityWordInfos.size() ;                                           //获取并收集每轮历史对话匹配集长度，后续用来计算个数占比
                if(historyMatchedREWISize != 0){
                    historyVolumeIncrementMap.put(lastNContextId, 1d / historyMatchedREWISize);
                }
            }
        }

        Map<Integer, Double> contextId_WeightMap = new HashMap<>();
        Integer maxValueContextId = null;                                                                              //最高匹配值的ContextId
        Double maxValue = 0d;                                                                                           //最高匹配值

        //2.准备好关联数据
        Map<CoordinateKey, List<ChangeParamMatchRateInfo>> hitREntityWordInfosMap = new HashMap<>();
        for (SentenceVector sentenceVector : sentenceVectors) {                                                         //句子端：查询本次检索出来的句子向量
            Integer sentenceVectorId = sentenceVector.getId();                                                          //句子向量ID
            List<List<REntityWordInfo>> currentREntityWordInfosList = sentenceVector.getrEntityWordInfosList();         //遍历当前句子向量里面的各个REWIs
            int currentSVSize = currentREntityWordInfosList.size();                                                     //句子向量长度

            for (List<REntityWordInfo> currentREntityWordInfos : currentREntityWordInfosList) {
                for (REntityWordInfo currentREntityWordInfo : currentREntityWordInfos) {
                    Integer currentEntityType = currentREntityWordInfo.getEntityType();
                    Integer currentEntityId = currentREntityWordInfo.getEntityId();
                    String currentEntityTypeId = currentREntityWordInfo.getEntityTypeId();                              //获取REWI的entityTypeId
                    Double currentWeight = currentREntityWordInfo.getWeightMap().get(sentenceVectorId);                 //获取本句子向量的此REWI的权重

                    List<REntityWordInfo> historyREntityWordInfos = historyREWIMap.get(currentEntityTypeId);            //历史端：尝试搜索此entityTypeId是否存在于历史匹配的REWI集合里面
                    if(historyREntityWordInfos != null && historyREntityWordInfos.size() > 0){                         //如果本次entityTypeId的REWI碰上了历史REWI集合
                        CoordinateKey coordinateKey = new CoordinateKey(sentenceVectorId, currentEntityType, currentEntityId);  //构建此唯一变量组作为key
                        List<ChangeParamMatchRateInfo> changeParamMatchRateInfos = hitREntityWordInfosMap.get(coordinateKey);
                        if(changeParamMatchRateInfos == null){
                            changeParamMatchRateInfos = new ArrayList<>();
                        }

                        for (REntityWordInfo historyREntityWordInfo : historyREntityWordInfos) {
                            Integer contextId = historyREntityWordInfo.getContextId();
                            Double volumeIncrement = historyVolumeIncrementMap.get(contextId);
                            Map<Integer, Double> weightMap = historyREntityWordInfo.getWeightMap();
                            Double historyWeight = weightMap.get(sentenceVectorId);

                            ChangeParamMatchRateInfo changeParamMatchRateInfo = new ChangeParamMatchRateInfo();

                            changeParamMatchRateInfo.setCoordinateKey(coordinateKey);
                            changeParamMatchRateInfo.setHistoryREntityWordInfo(historyREntityWordInfo);

                            currentWeight = currentWeight == null ? 0d : currentWeight;
                            changeParamMatchRateInfo.setSvVolumnOccupyRate(1d / currentSVSize);
                            changeParamMatchRateInfo.setSvWeightOccupyRate(currentWeight);

                            volumeIncrement = volumeIncrement == null ? 0d : volumeIncrement;
                            changeParamMatchRateInfo.setHistoryVolumnOccupyRate(volumeIncrement);
                            changeParamMatchRateInfo.setHistoryWeightOccupyRate(historyWeight);

                            double matchValue = contextId_WeightMap.get(contextId) + (1d / currentSVSize * currentWeight) + (volumeIncrement * historyWeight);
                            contextId_WeightMap.put(contextId, matchValue);                                             //统计值
                            if(matchValue > maxValue || (matchValue == maxValue && contextId > maxValueContextId)){
                                maxValueContextId = contextId;
                                maxValue = matchValue;
                            }
                            changeParamMatchRateInfos.add(changeParamMatchRateInfo);
                        }
                        hitREntityWordInfosMap.put(coordinateKey, changeParamMatchRateInfos);
                    }
                }
            }
        }



        SVRuleInfo optimalSvRuleInfo = null;

        return new ComponentBizResult("FPMC_S", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);             //此结果在本地和远程都要存储
    }
}
