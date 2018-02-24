package com.cooler.semantic.service.external.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.dao.LogDataProcessMapper;
import com.cooler.semantic.entity.*;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.ProcessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("processLogService")
public class ProcessLogServiceImpl implements ProcessLogService {

    private static Logger logger = LoggerFactory.getLogger(ProcessLogServiceImpl.class.getName());
    @Autowired
    private LogDataProcessMapper logDataProcessMapper;

    @Override
    public void writeLog(int logType, List<DataComponent> dataComponents, String processTrace) {
        switch (logType){
            case Constant.PROCESS_LOG_TEXT : {
                writeTextLog(dataComponents, processTrace);
                break;
            }
            case Constant.PROCESS_LOG_HTML : {
                writeHtmlLog(dataComponents, processTrace);
                break;
            }
            case Constant.PROCESS_LOG_DATA_BASE : {
                writeDataBaseLog(dataComponents, processTrace);
                break;
            }
            default : {
                writeTextLog(dataComponents, processTrace);
            }
        }
    }

    /**
     * 存储日志到数据库中
     * @param dataComponents
     * @param processTrace
     */
    private void writeDataBaseLog(List<DataComponent> dataComponents, String processTrace) {
        LogDataProcess logDataProcess = new LogDataProcess();
        logDataProcess.setDateTime(new Date());
        logDataProcess.setProcessTrace(processTrace);

        for (DataComponent dataComponent : dataComponents) {
            String dataComponentId = dataComponent.getId();
            Object data = dataComponent.getData();
            switch (dataComponentId){
                case "historyDataComponents" : {
                    List<DataComponent<SVRuleInfo>> historyDataComponents = (List<DataComponent<SVRuleInfo>>)data;
//                    System.out.println(JSON.toJSONString(historyDataComponents));
                    break;
                }
                case "semanticParserRequest" : {
                    SemanticParserRequest semanticParserRequest = (SemanticParserRequest)data;
                    List<Integer> accountIds = semanticParserRequest.getAccountIds();
                    Integer userId = semanticParserRequest.getUserId();
                    int contextId = semanticParserRequest.getContextId();
                    String cmd = semanticParserRequest.getCmd();
                    logDataProcess.setAccountId(accountIds.get(0));
                    logDataProcess.setUserId(userId);
                    logDataProcess.setContextId(contextId);
                    logDataProcess.setDetailContextOwner(Arrays.toString(accountIds.toArray()) + "_" + userId + "_" + contextId );
                    logDataProcess.setSentence(cmd);
                    logDataProcess.setConfigureParams(JSON.toJSONString(semanticParserRequest));
                    break;
                }
                case "semanticParserResponse" : {
                    SemanticParserResponse semanticParserResponse = (SemanticParserResponse) data;
                    String sentenceModified = semanticParserResponse.getSentenceModified();
                    String responseMsg = semanticParserResponse.getResponseMsg();
                    int state = semanticParserResponse.getState();
                    double score = semanticParserResponse.getScore();
                    int responseType = semanticParserResponse.getResponseType();
                    long responseTimestamp = semanticParserResponse.getResponseTimestamp();

                    logDataProcess.setSentenceModified(sentenceModified);
                    logDataProcess.setResponseMsg(responseMsg);
                    logDataProcess.setState(state);
                    logDataProcess.setScore(score);
                    logDataProcess.setResponseType(responseType);
                    logDataProcess.setResponseTimestamp(responseTimestamp + "");
                    break;
                }
                case "sentenceVectors" : {
                    List<SentenceVector> sentenceVectors = (List<SentenceVector>) data;
                    StringBuffer sv_ids_sb = new StringBuffer();
                    StringBuffer sv_words_sb = new StringBuffer();
                    StringBuffer sv_weights_sb = new StringBuffer();
                    for (SentenceVector sentenceVector : sentenceVectors) {
                        Integer sentenceVectorId = sentenceVector.getId();
                        List<String> words = sentenceVector.getWords();
                        List<Double> weights = sentenceVector.getWeights();
                        sv_ids_sb.append(sentenceVectorId).append(", ");
                        sv_words_sb.append(Arrays.toString(words.toArray())).append(", ");
                        sv_weights_sb.append(Arrays.toString(weights.toArray())).append(", ");
                    }
                    logDataProcess.setSvIds(sv_ids_sb.toString());
                    logDataProcess.setSvWords(sv_words_sb.toString());
                    logDataProcess.setSvWeights(sv_weights_sb.toString());
                    break;
                }
                case "optimalSvRuleInfo_LPM" : {
                    SVRuleInfo optimalSvRuleInfo_LPM = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo_LPM, logDataProcess, Constant.LPM);
                    break;
                }
                case "optimalSvRuleInfo_CPM" : {
                    SVRuleInfo optimalSvRuleInfo_CPM = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo_CPM, logDataProcess, Constant.CPM);
                    break;
                }
                case "optimalSvRuleInfo_FPM" : {
                    SVRuleInfo optimalSvRuleInfo_FPM = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo_FPM, logDataProcess, Constant.FPM);
                    break;
                }
                case "optimalSvRuleInfo" : {
                    SVRuleInfo optimalSvRuleInfo = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo, logDataProcess, Constant.OPTIMAL);
                    break;
                }

            }
        }
        logDataProcessMapper.insert(logDataProcess);
    }

    private void writeHtmlLog(List<DataComponent> dataComponents, String processTrace) {
    }

    private void writeTextLog(List<DataComponent> dataComponents, String processTrace) {
    }

    /**
     * 收集SVRuleInfo里面的值
     * @param svRuleInfo
     * @param logDataProcess
     * @param resultType
     */
    private void putSVRuleInfoParams(SVRuleInfo svRuleInfo, LogDataProcess logDataProcess, int resultType){
        if(svRuleInfo == null) return;         //这种情况有可能发生，当没有匹配到任何规则的时候，svRuleInfo将为空，有时候lastState<0时，下一轮对话尝试缺参匹配，下一轮如果跳到另一个场景了，那么缺参匹配就会产生空svRuleInfo
        Integer sentenceVectorId = svRuleInfo.getSentenceVectorId();

        StringBuffer rule_score_sb = new StringBuffer();
        StringBuffer matched_rew_sb = new StringBuffer();
        StringBuffer matched_rre_sb = new StringBuffer();                                                    //包含wordId_word_normalWord
        StringBuffer lacked_rre_sb = new StringBuffer();
        List<REntityWordInfo> matchedREntityWordInfos = svRuleInfo.getMatchedREntityWordInfos();
        List<RRuleEntity> matchedRRuleEntities = svRuleInfo.getMatchedRRuleEntities();
        List<RRuleEntity> lackedRRuleEntities = svRuleInfo.getLackedRRuleEntities();

        Integer ruleId = svRuleInfo.getRuleId();
        String ruleName = svRuleInfo.getRuleName();
        Integer algorithmType = svRuleInfo.getAlgorithmType();
        Double similarity = svRuleInfo.getSimilarity();
        Double runningAccuracyThreshold = svRuleInfo.getRunningAccuracyThreshold();
        rule_score_sb.append(ruleId).append("| ").append(ruleName).append("| ").append(algorithmType).append("| ").append(String.format("%.3f",similarity)).append("| ").append(String.format("%.3f",runningAccuracyThreshold));

        if(matchedREntityWordInfos != null && matchedREntityWordInfos.size() > 0){
            for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                String entityTypeId = matchedREntityWordInfo.getEntityTypeId();
                String entityName = matchedREntityWordInfo.getEntityName();
                Integer wordId = matchedREntityWordInfo.getWordId();
                String word = matchedREntityWordInfo.getWord();
//            String normalWord = matchedREntityWordInfo.getNormalWord();
                Double weight = matchedREntityWordInfo.getWeightMap().get(sentenceVectorId);
                weight = (weight == null ? 0d : weight);
                matched_rew_sb.append("(").append(entityTypeId).append("_").append(entityName).append(")(").append(String.format("%.3f", weight)).append(")(").append(wordId).append("_").append(word).append(")| ");
            }
        }

        if(matchedRRuleEntities != null && matchedRRuleEntities.size() > 0){
            for (RRuleEntity matchedRRuleEntity : matchedRRuleEntities) {
                String entityTypeId = matchedRRuleEntity.getEntityTypeId();
                String entityName = matchedRRuleEntity.getEntityName();
                Double weight = matchedRRuleEntity.getWeight();
                weight = (weight == null ? 0d : weight);
                matched_rre_sb.append("(").append(entityTypeId).append("_").append(entityName).append(")(").append(String.format("%.3f", weight)).append(")| ");
            }
        }
        if(lackedRRuleEntities != null && lackedRRuleEntities.size() > 0){
            for (RRuleEntity lackedRRuleEntity : lackedRRuleEntities) {
                String entityTypeId = lackedRRuleEntity.getEntityTypeId();
                String entityName = lackedRRuleEntity.getEntityName();
                Double weight = lackedRRuleEntity.getWeight();
                weight = (weight == null ? 0d : weight);
                lacked_rre_sb.append("(").append(entityTypeId).append("_").append(entityName).append(")(").append(String.format("%.3f", weight)).append(")| ");
            }
        }

        switch (resultType){
            case  Constant.LPM : {
                logDataProcess.setLpmRuleScore(rule_score_sb.toString());
                logDataProcess.setLpmMatchedRew(matched_rew_sb.toString());
                logDataProcess.setLpmMatchedRre(matched_rre_sb.toString());
                logDataProcess.setLpmLackedRre(lacked_rre_sb.toString());
                break;
            }
            case  Constant.CPM : {
                logDataProcess.setCpmRuleScore(rule_score_sb.toString());
                logDataProcess.setCpmMatchedRew(matched_rew_sb.toString());
                logDataProcess.setCpmMatchedRre(matched_rre_sb.toString());
                logDataProcess.setCpmLackedRre(lacked_rre_sb.toString());
                break;
            }
            case  Constant.FPM : {
                logDataProcess.setFpmRuleScore(rule_score_sb.toString());
                logDataProcess.setFpmMatchedRew(matched_rew_sb.toString());
                logDataProcess.setFpmMatchedRre(matched_rre_sb.toString());
                logDataProcess.setFpmLackedRre(lacked_rre_sb.toString());
                break;
            }
            case  Constant.OPTIMAL : {
                StringBuffer selectResultSB = new StringBuffer();
                Integer matchType = svRuleInfo.getMatchType();
                selectResultSB.append(matchType).append("| ")
                        .append(ruleId).append("| ")
                        .append(ruleName).append("| ")
                        .append(algorithmType).append("| ")
                        .append(String.format("%.3f",similarity)).append("| ")
                        .append(runningAccuracyThreshold);
                logDataProcess.setSelectResult(selectResultSB.toString());
                break;
            }
        }
    }
}
