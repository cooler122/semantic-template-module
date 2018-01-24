package com.cooler.semantic.service.external.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.dao.ProcessLogDataMapper;
import com.cooler.semantic.entity.ProcessLogData;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service("logService")
public class LogServiceImpl implements LogService {

    private static Logger logger = LoggerFactory.getLogger(LogServiceImpl.class.getName());
    @Autowired
    private ProcessLogDataMapper processLogDataMapper;

    @Override
    public void writeLog(int logType, List<DataComponent> dataComponents, String processTrace) {
        switch (logType){
            case Constant.TEXT_LOG : {
                writeTextLog(dataComponents, processTrace);
                break;
            }
            case Constant.HTML_LOG : {
                writeHtmlLog(dataComponents, processTrace);
                break;
            }
            case Constant.DATA_BASE_LOG : {
                writeDataBaseLog(dataComponents, processTrace);
                break;
            }
            default : {
                writeTextLog(dataComponents, processTrace);
            }
        }
    }

    private void writeDataBaseLog(List<DataComponent> dataComponents, String processTrace) {
        ProcessLogData processLogData = new ProcessLogData();
        processLogData.setProcessTrace(processTrace);
        for (DataComponent dataComponent : dataComponents) {
            String dataComponentId = dataComponent.getId();
            ContextOwner contextOwner = dataComponent.getContextOwner();
            String ownerIndex = contextOwner.getOwnerIndex();
            processLogData.setOwnerIndex(ownerIndex);

            Object data = dataComponent.getData();
            switch (dataComponentId){
                case "historyDataComponents" : {
                    List<DataComponent<SVRuleInfo>> historyDataComponents = (List<DataComponent<SVRuleInfo>>)data;
                    System.out.println(JSON.toJSONString(historyDataComponents));
                    break;
                }
                case "semanticParserRequest" : {
                    SemanticParserRequest semanticParserRequest = (SemanticParserRequest)data;
                    List<Integer> accountIds = semanticParserRequest.getAccountIds();
                    Integer userId = semanticParserRequest.getUserId();
                    int contextId = semanticParserRequest.getContextId();
                    String cmd = semanticParserRequest.getCmd();

                    processLogData.setDetailContextOwner(Arrays.toString(accountIds.toArray()) + "_" + userId + "_" + contextId );
                    processLogData.setSentence(cmd);
                    processLogData.setConfigureParams(JSON.toJSONString(semanticParserRequest));
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
                    processLogData.setSvIds(sv_ids_sb.toString());
                    processLogData.setSvWords(sv_words_sb.toString());
                    processLogData.setSvWeights(sv_weights_sb.toString());
                    break;
                }
                case "optimalSvRuleInfo_LPM" : {
                    SVRuleInfo optimalSvRuleInfo_LPM = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo_LPM, processLogData, Constant.LPM);
                    break;
                }
                case "optimalSvRuleInfo_CPM" : {
                    SVRuleInfo optimalSvRuleInfo_CPM = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo_CPM, processLogData, Constant.CPM);
                    break;
                }
                case "optimalSvRuleInfo_FPM" : {
                    SVRuleInfo optimalSvRuleInfo_FPM = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo_FPM, processLogData, Constant.FPM);
                    break;
                }
                case "optimalSvRuleInfo" : {
                    SVRuleInfo optimalSvRuleInfo = (SVRuleInfo) data;
                    putSVRuleInfoParams(optimalSvRuleInfo, processLogData, Constant.OPTIMAL);
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

                    processLogData.setSentenceModified(sentenceModified);
                    processLogData.setResponseMsg(responseMsg);
                    processLogData.setState(state);
                    processLogData.setScore(score);
                    processLogData.setResponseType(responseType);
                    processLogData.setResponseTimestamp(responseTimestamp + "");
                    break;
                }
            }
        }
        processLogDataMapper.insert(processLogData);
    }

    private void writeHtmlLog(List<DataComponent> dataComponents, String processTrace) {
    }

    private void writeTextLog(List<DataComponent> dataComponents, String processTrace) {
    }

    private void putSVRuleInfoParams(SVRuleInfo svRuleInfo, ProcessLogData processLogData, int resultType){
        Integer sentenceVectorId = svRuleInfo.getSentenceVectorId();
        StringBuffer rew_entitys_sb = new StringBuffer();                                                               //包含entityType_entityId_entityName
        StringBuffer rew_words_sb = new StringBuffer();                                                                 //包含wordId_word_normalWord
        StringBuffer rew_weights_sb = new StringBuffer();
        List<REntityWordInfo> matchedREntityWordInfos = svRuleInfo.getMatchedREntityWordInfos();
        for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
            String entityTypeId = matchedREntityWordInfo.getEntityTypeId();
            String entityName = matchedREntityWordInfo.getEntityName();
            Integer wordId = matchedREntityWordInfo.getWordId();
            String word = matchedREntityWordInfo.getWord();
//            String normalWord = matchedREntityWordInfo.getNormalWord();
            Double weight = matchedREntityWordInfo.getWeightMap().get(sentenceVectorId);
            weight = (weight == null ? 0d : weight);

            rew_entitys_sb.append(entityTypeId).append("_").append(entityName).append("| ");
            rew_words_sb.append(wordId).append("_").append(word).append("| ");
            rew_weights_sb.append(String.format("%.3f", weight)).append("| ");
        }
        switch (resultType){
            case  Constant.LPM : {
                processLogData.setLpmRewEntitys(rew_entitys_sb.toString());
                processLogData.setLpmRewWords(rew_words_sb.toString());
                processLogData.setLpmRewWeights(rew_weights_sb.toString());
                break;
            }
            case  Constant.CPM : {
                processLogData.setCpmRewEntitys(rew_entitys_sb.toString());
                processLogData.setCpmRewWords(rew_words_sb.toString());
                processLogData.setCpmRewWeights(rew_weights_sb.toString());
                break;
            }
            case  Constant.FPM : {
                processLogData.setFpmRewEntitys(rew_entitys_sb.toString());
                processLogData.setFpmRewWords(rew_words_sb.toString());
                processLogData.setFpmRewWeights(rew_weights_sb.toString());
                break;
            }
            case  Constant.OPTIMAL : {
                processLogData.setOptimalRewEntitys(rew_entitys_sb.toString());
                processLogData.setOptimalRewWords(rew_words_sb.toString());
                processLogData.setOptimalRewWeights(rew_weights_sb.toString());
                break;
            }
        }
    }
}
