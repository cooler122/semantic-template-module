package com.cooler.semantic.facade;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.api.SemanticParserFacade;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.SentenceInfo;
import com.cooler.semantic.service.EntityService;
import com.cooler.semantic.service.PreprocessingService;
import com.cooler.semantic.service.SentencePrecessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("semanticParserFacade")
public class SemanticParserFacadeImpl implements SemanticParserFacade {

    private static Logger logger = LoggerFactory.getLogger(SemanticParserFacadeImpl.class.getName());

    @Autowired
    private PreprocessingService preprocessingService;      //预处理组件（校验、查账户、设置）
    @Autowired
    private SentencePrecessService sentencePrecessService;         //句子处理组件（分词、权重、词频）
    @Autowired
    private EntityService entityService;                         //实体处理组件


    public SemanticParserResponse semanticParse(SemanticParserRequest request) {

        //1.校验和配置 （目标：1.校验输入参数合法性     2.获取accountId权限，并放入request      3.查询出accountId对应的配置参数，并放到request中）
        logger.info("SO-1.-------------------校验和配置");
        if(!preprocessingService.preprocess(request)){
            return null;                                                        //此处返回一个空结果体
        }

        //2.句子处理    （目标：拿到一个句子最佳组合方式，当前只是一个组合方式，但后续会支持多种高分值组合，使用大数据计算的词频，保证分词的质量）
        logger.info("SO-2.-------------------句子处理");
        List<SentenceInfo> sentenceInfos = sentencePrecessService.sentenceProcess(request);

        //3.词语-实体检索
        logger.info("SO-3.-------------------词语-实体检索");
        List<SentenceInfo> sentenceInfosForSearch = entityService.entitySearch(request, sentenceInfos);
        logger.info("检索的sentenceInfo集合： " + JSON.toJSONString(sentenceInfosForSearch));

        return null;
    }
}
