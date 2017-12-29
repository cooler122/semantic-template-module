//package com.cooler.semantic.facade.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.cooler.semantic.entity.SemanticParserRequest;
//import com.cooler.semantic.entity.SemanticParserResponse;
//import com.cooler.semantic.facade.SemanticParserFacade;
//import com.cooler.semantic.model.SentenceVector;
//import com.cooler.semantic.service.external.EntitySearchService;
//import com.cooler.semantic.service.external.PreprocessingService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component("semanticParserFacade1")
//public class SemanticParserFacadeImpl1 implements SemanticParserFacade {
//
//    private static Logger logger = LoggerFactory.getLogger(SemanticParserFacadeImpl1.class.getName());
//
//    @Autowired
//    private PreprocessingService preprocessingService;      //预处理组件（校验、查账户、设置）
//    @Autowired
//    private SentencePrecessService sentencePrecessService;         //句子处理组件（分词、权重、词频）
//    @Autowired
//    private EntitySearchService entitySearchService;                         //实体处理组件
//
//
//    public SemanticParserResponse semanticParse(SemanticParserRequest request) {
//
//        int lastState = request.getLastState();                                     //上次上下文状态
//
//        //SO-1.校验和配置 （目标：1.校验输入参数合法性     2.获取accountId权限，并放入request      3.查询出accountId对应的配置参数，并放到request中）
//        logger.info("SO-1.-------------------校验和配置");
//        if(!preprocessingService.preprocess(request)){
//            return null;                                                        //此处返回一个空结果体
//        }
//
//        //SO-2.句子处理    （目标：拿到一个句子最佳组合方式，当前只是一个组合方式，但后续会支持多种高分值组合，使用大数据计算的词频，保证分词的质量）
//        logger.info("SO-2.-------------------句子处理");
//        List<SentenceVector> sentenceVectors = sentencePrecessService.sentenceProcess(request);
//
//        //SO-3.词语-实体检索
//        logger.info("SO-3.-------------------词语-实体检索");
//        List<SentenceVector> sentenceVectorsWithEntity = entitySearchService.entitySearch(request, sentenceVectors);
//        logger.info("检索的sentenceInfo集合： " + JSON.toJSONString(sentenceVectorsWithEntity));
//
//        //SO-4、SO-5.缺参匹配
//        List<SentenceVector> sentenceVectorsFromLPM = null;                         //缺参匹配的规则集
//        if(lastState < 0){                                                                                          //D1-Y:----上次状态<0，进入缺参匹配
//            logger.info("SO-4.-------------------缺参匹配--a.获取上次保存的缺参匹配的半匹配状态的规则数据体");
//
//            logger.info("SO-5.-------------------缺参匹配--b.高分缺参匹配集和实体向量集的缺参匹配，得到缺参匹配高分规则RS");
//
//        }else{                                                                                                      //D1-N:----上次状态>=，不进入缺参匹配
//
//        }
//
//        //SO-6、SO-7.换参匹配
//        if(sentenceVectorsFromLPM == null || sentenceVectorsFromLPM.size() == 0 /** || ( 数量>0 && UPR < ? || SUR < ?)*/){        //D2-Y:----缺参匹配无效的情况
//            List<SentenceVector> sentenceVectorsFromCPM = null;
//            if(/* TODO:可换参数数量 > 0 */true){                                                                                  //D3-Y:----可换参数数量 > 0，进入换参匹配
//                logger.info("SO-6.-------------------换参匹配--a.获取前面保存的记忆数据，包括（场景、规则、实体、运行中意图集）");
//
//                logger.info("SO-7.-------------------换参匹配--b.记忆数据MD和此次实体向量集V，参数替换和换参匹配，得到换参匹配规则集B");
//                if(true /*换参匹配无效*/){                                                                                                //D4-Y:----换参匹配无效，进入全参匹配
//                    logger.info("SO-8.-------------------全参匹配--a.根据实体向量V中的实体集，查询出所有相关的规则列表集");
//
//                    logger.info("SO-9.-------------------全参匹配--b.对多组规则，按各个维度值来归并规则列表，得到最佳规则列表（分最广覆盖度和最多覆盖度）");
//
//                    logger.info("SO-10.-------------------全参匹配--c.按照规则阈值过滤，按照规则场景门过滤，得到最佳规则集BRs");
//                }
//            }else{                                                                                                                   //D3-N:----可换参数数量 = 0，不进入换参匹配
//
//            }
//
//
//
//        }else{                                                                                                                     //D2-N:----缺参匹配有效，则跳过换参和全参匹配
//
//        }
//
//
//
//        return null;
//    }
//}
