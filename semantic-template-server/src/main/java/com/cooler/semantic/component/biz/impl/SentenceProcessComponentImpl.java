package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.WeightCalculateService;
import com.cooler.semantic.service.WordSegregateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("sentenceProcessComponent")
public class SentenceProcessComponentImpl extends FunctionComponentBase<SemanticParserRequest, List<SentenceVector>> {

    private static Logger logger = LoggerFactory.getLogger(SentenceProcessComponentImpl.class.getName());

    @Autowired
    private WordSegregateService wordSegregateService;      //分词组件
    @Autowired
    private WeightCalculateService weightCalculateService;  //权重计算组件

    public SentenceProcessComponentImpl() {
        super("SPC", "SO-2", "semanticParserRequest", "sentenceVectors");
    }

    @Override
    protected ComponentBizResult<List<SentenceVector>> runBiz(SemanticParserRequest bizData) {
        logger.info("SO_2.句子处理");

        //SO-2-1.多重分词（包含标词）
        logger.info("SO-2-1.分词...");
        String cmd = bizData.getCmd();
        Integer accountId = bizData.getAccountIds().get(0);              //取出第一个accountId，进行分词
        List<SentenceVector> sentenceVectors = wordSegregateService.wordSegregate(cmd, accountId, null, Arrays.asList(1), true);

        if (null == sentenceVectors && false) {                        //TODO:注意这里如果添加&&false，则是一个测试状态，当上门的方法还没有做好的时候，添加这个为了放开口子向下执行。
            logger.error("分词错误，原始句子: " + cmd);
            return new ComponentBizResult("SPC_E", false, new ArrayList<SentenceVector>());
        }

        //SO-2-2.计算权重
        logger.info("SO-2-2.计算权重...");
        weightCalculateService.calculateWeight(sentenceVectors);          //此方法为分词段设置权重，并返回权重数组，如果分词不成功，则返回默认分词权重数组(此为句子内部各个此的权重值，可以作为后续相似度计算标准)

        return new ComponentBizResult("SPC_S", true, sentenceVectors);
    }
}
