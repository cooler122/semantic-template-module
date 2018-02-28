package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.model.console.AmnesiacData;
import com.cooler.semantic.model.console.CoupleAlterationRateData;
import com.cooler.semantic.model.CalculationLogParam_LPM;
import com.cooler.semantic.model.console.SimilarityCalculationData_LPM;
import com.cooler.semantic.service.external.RedisService;
import com.cooler.semantic.service.external.SimilarityCalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component("lackParamMatchComponent")
public class LackParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(LackParamMatchComponentImpl.class.getName());
    @Autowired
    private SimilarityCalculateService similarityCalculateService;

    @Autowired
    private RedisService redisService;

    public enum CalculationLogDataType { sentenceVectors, historySVRuleInfoMap , canceledLPMContextIdSet, coupleAlterationRateDatas , amnesiacDatas , similarityCalculationDatas , lpmSVRuleInfo };

    public LackParamMatchComponentImpl() {
        super("LPMC", "sentenceVectors", "optimalSvRuleInfo_LPM");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.trace("LPMC.缺参匹配");

        Integer accountId = contextOwner.getCoreAccountId();
        Integer userId = contextOwner.getUserId();

        //0.准备好用户配置数据
        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();
        int algorithmType = request.getAlgorithmType();                                                                 //由用户选择使用哪种算法（当前1~5种， 默认JACCARD_VOLUME_WEIGHT_RATE）
        int calculationLogType = request.getCalculationLogType();                                                       //由用户选择何种类型打印计算日志
        CalculationLogParam_LPM calculationLogParam_lpm = null;
        List<CoupleAlterationRateData> coupleAlterationRateDatas = null;
        List<AmnesiacData> amnesiacDatas = null;
        List<SimilarityCalculationData_LPM> similarityCalculationDataLPMS = null;
        Integer currentContextId = contextOwner.getContextId();
        SVRuleInfo lpm_optimalSvRuleInfo = null;                                                                        //选择的最匹配的历史记录
        Double maxSimilarityDistance = 0d;
        Integer bestContextId = null;

        if(calculationLogType != Constant.NO_CALCULATION_LOG){
            calculationLogParam_lpm = new CalculationLogParam_LPM();
        }
        this.saveCalculationLog(calculationLogType, CalculationLogDataType.sentenceVectors , calculationLogParam_lpm, sentenceVectors);                  //*****************缺参日志准备：1-1

        String canceledLPMContextIdSet_DataName = accountId + "_" + userId + "_canceledLPMContextIdSet";
        DataComponentBase<Set<Integer>> canceledLPMContextIdData = (DataComponentBase<Set<Integer>>)redisService.getCacheObject(canceledLPMContextIdSet_DataName);
        Set<Integer> canceledLPMContextIdSet = null;
        if(canceledLPMContextIdData != null){
            canceledLPMContextIdSet = canceledLPMContextIdData.getData();
            this.saveCalculationLog(calculationLogType, CalculationLogDataType.canceledLPMContextIdSet , calculationLogParam_lpm, canceledLPMContextIdSet);     //*****************缺参日志准备：1-1
        }else{
            canceledLPMContextIdSet = new HashSet();
        }

        //1.准备好历史数据
        DataComponent<List<DataComponent<SVRuleInfo>>> historyBigDataComponent = componentConstant.getDataComponent("historyDataComponents", contextOwner);
        List<DataComponent<SVRuleInfo>> historyDataComponents = historyBigDataComponent.getData();
        Map<Integer, DataComponent<SVRuleInfo>> historyDataComponentMap = new HashMap<>();                              //将历史数据放入Map中，以备后用
        Map<Integer, SVRuleInfo> historySVRuleInfoMap = new HashMap<>();
        for (DataComponent<SVRuleInfo> historyDataComponent : historyDataComponents) {
            if(historyDataComponent != null && historyDataComponent.getData() != null){
                Integer historyContextId = historyDataComponent.getContextOwner().getContextId();
                if(!canceledLPMContextIdSet.contains(historyContextId)){                                                //上下文ID需要不在缺参排除列表中
                    historyDataComponentMap.put(historyContextId, historyDataComponent);
                    SVRuleInfo historySVRuleInfoData = historyDataComponent.getData();
                    historySVRuleInfoMap.put(historyContextId, historySVRuleInfoData);
                }
            }
        }
        this.saveCalculationLog(calculationLogType, CalculationLogDataType.historySVRuleInfoMap , calculationLogParam_lpm, historySVRuleInfoMap);        //*****************缺参日志准备：1-2

        //2.遍历本轮参数的每个句子向量
        for (SentenceVector sentenceVector : sentenceVectors) {
            //2.1.准备好句子端的REWIs数据，将其放到一个Map中，以备后面查询
            Integer sentenceVectorId = sentenceVector.getId();                                                          //分词模式编号
            Map<String, REntityWordInfo> rEntityWordInfoMap = new HashMap<>();                                          //此分词模式下所得到的所有REW，放到一个Map中
            List<List<REntityWordInfo>> rEntityWordInfosList = sentenceVector.getrEntityWordInfosList();                //句子向量中分词段归属的实体集群
            for (List<REntityWordInfo> rEntityWordInfos : rEntityWordInfosList) {
                for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                    rEntityWordInfoMap.put(rEntityWordInfo.getEntityTypeId(), rEntityWordInfo);
                }
            }

            //2.2.开始逐一遍历近5轮上下文（当然是缺参状态下的上下文），来匹配当前每一个句子向量
            for (Integer historyContextId : historyDataComponentMap.keySet()) {
                DataComponent<SVRuleInfo> historyData = historyDataComponentMap.get(historyContextId);                  //从历史对话记录Map里面取出数据
                if(historyData != null) {
                    //2.2.1.取出历史上下文的SVRuleInfo绑定数据
                    SVRuleInfo historySvRuleInfo = historyData.getData();                                               //取出历史绑定体SVRuleInfo数据
                    Double historySimilarity = historySvRuleInfo.getSimilarity();                                       //历史相似度
                    historySvRuleInfo.setSentenceVectorId(sentenceVectorId);                                            //历史SVRuleInfo尝试和本次各个分词模式合并，先将历史SVRuleInfo的分词模式编号sentenceVectorId和本次循环的分词模式同步
                    List<REntityWordInfo> historyMatchedREntityWordInfos = historySvRuleInfo.getMatchedREntityWordInfos();  //第i轮对话，匹配上的REW集合
                    List<REntityWordInfo> combinedREntityWordInfos = new ArrayList<>();
                    List<RRuleEntity> historyMatchedRRuleEntities = historySvRuleInfo.getMatchedRRuleEntities();        //第i轮对话，匹配上的RRE集合
                    List<RRuleEntity> historyLackedRRuleEntities = historySvRuleInfo.getLackedRRuleEntities();          //第i轮对话，缺失的RRE集合

                    if(historyLackedRRuleEntities != null && historyLackedRRuleEntities.size() > 0){                   //看看当前选定的上下文是否有缺失参数，有的话才进行缺参匹配
                        int combineLackEntityCount = 0;                                                                 //可以合并的缺失实体数量
                        double totalUsedCurrSVInfoWeights = 0d;                                                        //本轮 句子向量端 的权重累积值
                        double totalUsedHistoryRuleWeights = 0d;                                                       //历史 规则端 的权重已经匹配的累积值
                        double totalUsedCurrentRuleWeights = 0d;                                                       //本次 规则端 的权重总提升值

                        //2.2.2.计算历史 规则端的匹配上的实体权重累积值
                        if(historyMatchedRRuleEntities != null && historyMatchedRRuleEntities.size() > 0){
                            for (RRuleEntity historyMatchedRRuleEntity : historyMatchedRRuleEntities) {
                                totalUsedHistoryRuleWeights += historyMatchedRRuleEntity.getWeight();                   //第i轮对话，规则端已经匹配上的实体权重累积起来，先算 totalUsedHistoryRuleWeights
                            }
                        }else{
                            historyMatchedRRuleEntities = new ArrayList<>();                                            //下面还需要用到这个，但这个可能为null，所以还是要新设置对象
                        }
                        //2.2.3.（缺参合并）遍历历史缺失参数集，来收集本次能合并到历史缺参规则中的分词REW，并记录权重累积值，历史数据集合修改，以及记录匹配数量
                        for(RRuleEntity historyLackedRRuleEntity : historyLackedRRuleEntities){                         //遍历缺失实体集合
                            String entityTypeId = historyLackedRRuleEntity.getEntityTypeId();                           //取出某一个缺失实体
                            REntityWordInfo rEntityWordInfo = rEntityWordInfoMap.get(entityTypeId);                     //在本轮对话获取的实体Map中查找，看看能否查找成功
                            if(rEntityWordInfo != null){                                                                //不为空，则表示查找成功，尝试合并
                                Map<Integer, Double> weightMap = rEntityWordInfo.getWeightMap();                        //当前句子向量中REW的weight
                                Double sv_weight = weightMap.get(sentenceVectorId);                                     //获取当前句子向量的此REW的句子端weight
                                totalUsedCurrSVInfoWeights += sv_weight != null ? sv_weight : 0d;                       //第i轮对话，句子向量端 已经匹配上的实体权重累积起来，算totalUsedCurrSVInfoWeights

                                historyMatchedREntityWordInfos.add(rEntityWordInfo);                                    //历史REW集合添加这个匹配上的REW（注意，此集合如果添加上新元素，则集合中将有两个contextId版本的REW对象，后续有区分）
                                historyMatchedRRuleEntities.add(historyLackedRRuleEntity);                              //历史RRE集合添加这个匹配上的RRE

                                totalUsedCurrentRuleWeights += historyLackedRRuleEntity.getWeight();                    //规则提升值累积
                                combineLackEntityCount ++;                                                              //合并的实体数量加1

                                if(calculationLogType != Constant.NO_CALCULATION_LOG){                                  //*****************缺参日志准备：2-3 集合添加
                                    combinedREntityWordInfos.add(rEntityWordInfo);
                                }
                            }
                        }
                        historyLackedRRuleEntities.removeAll(historyMatchedRRuleEntities);                              //历史缺参RRE集合，删除所有匹配上的RRE集合

                                                                                                                        //1.如果当前对话产生的REW没有一个被当前选定的历史缺参REW集合匹配上，那么说明当前对话接不上历史对话；
                                                                                                                        //2.对于句子向量端：如果当前句子向量的权重累积值<0.2d，说明用上的分词段没太大意义。
                                                                                                                        //3.对于规则端：如果历史规则的权重提升值< 0.1d，说明用上的分词段在规则端起到的作用太小，也没有太大意义。
                                                                                                                        // 遇此三种情况，此处1 || (2 && 3)，就continue到下一轮（当然这些限制条件可以选择使用）
                        if(combineLackEntityCount == 0 || (totalUsedCurrSVInfoWeights < 0.2d && totalUsedCurrentRuleWeights < 0.1d)) continue;

                        //2.2.4.将变化后的历史RRE集合放到一个Map中，以备后续查找和传递
                        Map<String, RRuleEntity> historyMatchedRRuleEntityMap = new HashMap<>();                        //构建一个Map，将所有历史版本的缺参RRE放到这个Map，以便后面查找，这就代表了Rule端的RRE集合了，会传入相似度算法进行计算了
                        for (RRuleEntity historyMatchedRRuleEntity : historyMatchedRRuleEntities) {
                            String entityTypeId = historyMatchedRRuleEntity.getEntityTypeId();
                            RRuleEntity rRuleEntity = new RRuleEntity();
                            BeanUtils.copyProperties(historyMatchedRRuleEntity, rRuleEntity);                           //这里需要深克隆，留下原来的值，因为后面这个historyMatchedRRuleEntity会被重新设置权重
                            historyMatchedRRuleEntityMap.put(entityTypeId, rRuleEntity);
                        }

                        //2.2.5.计算历史上下文的失忆系数
                        double amnesiacCoefficient = getAmnesiacCoefficient(-1 * (currentContextId - historyContextId));               //失忆系数


                        //2.2.6.（关键代码，重设合并句子权重）遍历添加了新REW后的历史REW集合，为每一个REW设置合并分词段后的权重
                        for (REntityWordInfo historyMatchedREntityWordInfo : historyMatchedREntityWordInfos) {          //此historyMatchedREntityWordInfos在后面的缺参相似度计算中将要使用，在这个循环中将为其每一个REW重新规制权重
                            //2.2.6.1.获取每一个REW对象的相关信息
                            Integer rewContextId = historyMatchedREntityWordInfo.getContextId();                        //上下文版本号contextId（用来区分此REW是当前版本还是历史版本）
                            String entityTypeId = historyMatchedREntityWordInfo.getEntityTypeId();                      //REW的唯一标识entityTypeId
                            Map<Integer, Double> historyMatchedREntityWeightMap = historyMatchedREntityWordInfo.getWeightMap();     //REW的权重Map<sentenceVectorId, weight>

                            //2.2.6.2.如果这个REW是当前对话的句子向量中产生的REW，按公式设置相关实体的权重
                            if(rewContextId == currentContextId){                                                              //如果此REW版本等于当前对话版本，那么它就是本轮句子产生的REW（它应该来源于本轮对话的句子向量sv）
                                REntityWordInfo currREntityWordInfo = historyMatchedREntityWordInfo;                    //此REW是本轮对话产生的REW，故而赋值给currREntityWordInfo，虽然是句废代码，但标识此含义。
                                Map<Integer, Double> weightMap = currREntityWordInfo.getWeightMap();                    //取出其权重Map
                                Double rewWeight = weightMap.get(sentenceVectorId);                                     //找出针对当前句子向量的此REW的权重

                                historyMatchedREntityWeightMap.put(sentenceVectorId, rewWeight * (1 - (amnesiacCoefficient * totalUsedHistoryRuleWeights)) / totalUsedCurrSVInfoWeights);   //从新为此REW设置针对当前句子向量的权重值（此有公式）
                                System.out.println("contextId: " + rewContextId + "(current)  " + historyMatchedREntityWordInfo.getEntityName() + " : " + rewWeight + " * " + "(1 - (" + amnesiacCoefficient + " * " + totalUsedHistoryRuleWeights + " )) / " +  totalUsedCurrSVInfoWeights);
                            }
                            //2.2.6.3.如果这个REW是历史对话中存储的REW，按公式设置它权重
                            else{                                                                                       //如果此REW版本不等于本轮对话版本号，那么它就是历史对话产生的REW（它来源于历史对话的半匹配规则rule）
                                RRuleEntity historyMatchedRRuleEntity = historyMatchedRRuleEntityMap.get(entityTypeId); //取出上面准备好的历史RRE隐射的RRE
                                Double historyMatchedRRuleEntityWeight = historyMatchedRRuleEntity.getWeight();         //获取其权重

                                historyMatchedREntityWeightMap.put(sentenceVectorId, historyMatchedRRuleEntityWeight * amnesiacCoefficient);        //从新为此REW设置针对当前句子向量的权重值（此有公式）
                                System.out.println("contextId: " + rewContextId + "(history)  " + historyMatchedREntityWordInfo.getEntityName() + " : " + historyMatchedRRuleEntityWeight + " * " + amnesiacCoefficient);
                            }
                        }

                        //2.2.7.计算缺参相似度
                        SimilarityCalculationData_LPM similarityCalculationDataLPM = null;
                        if(calculationLogType != Constant.NO_CALCULATION_LOG){
                            similarityCalculationDataLPM = new SimilarityCalculationData_LPM();
                            similarityCalculationDataLPM.setSentenceVectorId(sentenceVectorId);
                            similarityCalculationDataLPM.setContextId(currentContextId);
                            similarityCalculationDataLPM.setSvInputREWIs(rEntityWordInfosList);
                            similarityCalculationDataLPM.setRuleInputREWIs(historyMatchedREntityWordInfos);
                            similarityCalculationDataLPM.setAlgorithmType(algorithmType);
                            similarityCalculationDataLPM.setAlgorithmFormula(algorithmType + " 类型的相似度公式");
                        }
                        SVRuleInfo resultSvRuleInfo = similarityCalculateService.similarityCalculate_LPM(algorithmType, historySvRuleInfo, historyMatchedRRuleEntityMap, similarityCalculationDataLPM);      //计算相似度，historySvRuleInfo -> historyMatchedREntityWordInfos -> 相关的historyMatchedREntityWeightMap中的权重已经改变
//                        System.out.println(algorithmType + "\n" + JSON.toJSONString(historySvRuleInfo) + "\n" + JSON.toJSONString(historyMatchedRRuleEntityMap) + "\n --- > \n" + JSON.toJSONString(resultSvRuleInfo));
                        Double currentSimilarity = resultSvRuleInfo.getSimilarity();

                        //2.2.8.计算相似度提升值，进而选择最佳缺参匹配SVRuleInfo结果
                        double similarityDistance = currentSimilarity - historySimilarity;
                        if(similarityDistance > maxSimilarityDistance || (similarityDistance == maxSimilarityDistance && bestContextId < historyContextId)){
                            maxSimilarityDistance = similarityDistance;
                            lpm_optimalSvRuleInfo = resultSvRuleInfo;
                            bestContextId = historyContextId;
                        }

                        //2.2.9.制作缺参匹配的日志
                        if(calculationLogType != Constant.NO_CALCULATION_LOG){
                            if(coupleAlterationRateDatas == null){
                                coupleAlterationRateDatas = new ArrayList<>();
                            }
                            CoupleAlterationRateData coupleAlterationRateData = new CoupleAlterationRateData();             //当前sentenceVectorId和historyContextId下的匹配结果
                            coupleAlterationRateData.setSentenceVectorId(sentenceVectorId);                                 //*****************缺参日志准备：2-1
                            coupleAlterationRateData.setContextId(historyContextId);                                        //*****************缺参日志准备：2-2
                            coupleAlterationRateData.setCombinedREntityWordInfos(combinedREntityWordInfos);                 //*****************缺参日志准备：2-3
                            coupleAlterationRateData.setSvWeightOccupyRate(totalUsedCurrSVInfoWeights);                     //*****************缺参日志准备：2-4
                            coupleAlterationRateData.setRuleWeightOccupyRate(totalUsedCurrentRuleWeights);                  //*****************缺参日志准备：2-5
                            coupleAlterationRateData.setCombinedEntityCountAccuracyThreshold(1);                            //*****************缺参日志准备：2-6   //TODO:以后由用户自己设置
                            coupleAlterationRateData.setWeightRateAccuracyThreshold4SV(0.2d);                               //*****************缺参日志准备：2-7   //TODO:以后由用户自己设置
                            coupleAlterationRateData.setWeightRateAccuracyThreshold4Rule(0.1d);                             //*****************缺参日志准备：2-8   //TODO:以后由用户自己设置
                            boolean isCrossed = (combinedREntityWordInfos != null ? combinedREntityWordInfos.size() : 0) >= 1 && totalUsedCurrSVInfoWeights >= 0.2d && totalUsedCurrentRuleWeights >= 0.1d;
                            coupleAlterationRateData.setCrossed(isCrossed);                                                 //*****************缺参日志准备：2-9
                            coupleAlterationRateDatas.add(coupleAlterationRateData);

                            if(amnesiacDatas == null){
                                amnesiacDatas = new ArrayList<>();
                            }
                            AmnesiacData amnesiacData = new AmnesiacData();
                            amnesiacData.setCurrentContextId(currentContextId);
                            amnesiacData.setHistoryContextId(historyContextId);
                            amnesiacData.setContextDistance(currentContextId - historyContextId);
                            amnesiacData.setAmnesiacAlgorithmType(1);                                                   //TODO:实际上当前失忆算法只有一种，如果有必要，以后多添加一点算法
                            amnesiacData.setBaseNumber(0.6);                                                            //TODO:以后这个也由用户自己设置
                            amnesiacData.setCoefficient(amnesiacCoefficient);
                            amnesiacDatas.add(amnesiacData);

                            if(similarityCalculationDataLPMS == null){
                                similarityCalculationDataLPMS = new ArrayList<>();
                            }
                            similarityCalculationDataLPMS.add(similarityCalculationDataLPM);

                        }
                    }
                }
                //如果sentenceVectorId和historyContextId下的数据无法匹配，这是更多的情况，则不会有这种日志数据被记录
            }
        }

        this.saveCalculationLog(calculationLogType, CalculationLogDataType.coupleAlterationRateDatas , calculationLogParam_lpm, coupleAlterationRateDatas);          //*****************缺参日志准备：2
        this.saveCalculationLog(calculationLogType, CalculationLogDataType.amnesiacDatas , calculationLogParam_lpm, amnesiacDatas);                                  //*****************缺参日志准备：3
        this.saveCalculationLog(calculationLogType, CalculationLogDataType.similarityCalculationDatas , calculationLogParam_lpm, similarityCalculationDataLPMS);        //*****************缺参日志准备：4


        //3.此处缺参匹配已经匹配成功，既然已经选定了一个SVRuleInfo对象作为lpm_optimalSvRuleInfo，那么要完善此处的lpm_optimalSvRuleInfo内部结构体
        if(lpm_optimalSvRuleInfo != null){
            lpm_optimalSvRuleInfo.setMatchType(Constant.LPM);                                                           //设置匹配类型
            lpm_optimalSvRuleInfo.setAlgorithmType(algorithmType);                                                      //设置算法类型
            List<REntityWordInfo> matchedREntityWordInfos = lpm_optimalSvRuleInfo.getMatchedREntityWordInfos();
            List<String> wordsModified = new ArrayList<>();
            for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                wordsModified.add(matchedREntityWordInfo.getWord());
            }
            lpm_optimalSvRuleInfo.setWords(wordsModified);

            this.saveCalculationLog(calculationLogType, CalculationLogDataType.lpmSVRuleInfo , calculationLogParam_lpm, lpm_optimalSvRuleInfo);        //*****************缺参日志准备：5
            if(calculationLogType != Constant.NO_CALCULATION_LOG){
                componentConstant.putDataComponent(new DataComponentBase("CalculationLogParam_LPM", contextOwner, "String", JSON.toJSONString(calculationLogParam_lpm)));     //保存缺参匹配的计算型日志
            }

            canceledLPMContextIdSet.add(bestContextId);
            redisService.setCacheObject(canceledLPMContextIdSet_DataName, new DataComponentBase(outputDataBeanId, contextOwner, HashSet.class.getSimpleName(), canceledLPMContextIdSet));   //此处保存被选中的contextId到redis，下次使用时进行排查，进而避免无用匹配
        }

        return new ComponentBizResult("LPMC_S", Constant.STORE_LOCAL_REMOTE, lpm_optimalSvRuleInfo);      //无论结果是否为null，都要保存，此结果在本地和远程都要存储;
    }

    /**
     * 根据上下文轮次，获取失忆系数（当前以0.6的指数函数作为失忆函数，0.6算是失忆倍数）
     * @param historyRoundDistance 上下文轮次，距离当前上下文编号的距离
     * @return
     */
    private double getAmnesiacCoefficient(int historyRoundDistance){
        return Math.pow(0.6, -1 * historyRoundDistance);
    }

    /**
     * 保存计算日志的数据
     * @param calculationLogType
     * @param calculationLogDataType
     * @param calculationLogParamLPM
     * @param data
     */
    private void saveCalculationLog(int calculationLogType, CalculationLogDataType calculationLogDataType, CalculationLogParam_LPM calculationLogParamLPM, Object data){
        if(calculationLogType != Constant.NO_CALCULATION_LOG && data != null){
            switch (calculationLogDataType){
                case sentenceVectors : {
                    calculationLogParamLPM.setSentenceVectors((List<SentenceVector>)data);
                    break;
                }
                case historySVRuleInfoMap : {
                    calculationLogParamLPM.setHistorySVRuleInfoMap((Map<Integer, SVRuleInfo>)data);
                    break;
                }
                case canceledLPMContextIdSet : {
                    calculationLogParamLPM.setCanceledLPMContextIdSet((Set)data);
                    break;
                }
                case coupleAlterationRateDatas : {
                    calculationLogParamLPM.setCoupleAlterationRateDatas((List<CoupleAlterationRateData>)data);
                    break;
                }
                case amnesiacDatas : {
                    calculationLogParamLPM.setAmnesiacDatas((List<AmnesiacData>)data);
                    break;
                }
                case similarityCalculationDatas : {
                    calculationLogParamLPM.setSimilarityCalculationDataLPMS((List<SimilarityCalculationData_LPM>)data);
                    break;
                }
                case lpmSVRuleInfo : {
                    calculationLogParamLPM.setLpmSVRuleInfo((SVRuleInfo)data);
                    break;
                }
            }
        }
    }

}
