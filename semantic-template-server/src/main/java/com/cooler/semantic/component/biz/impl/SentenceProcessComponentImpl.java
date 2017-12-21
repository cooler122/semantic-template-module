package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.biz.SentenceProcessComponent;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.IOType;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.WeightCalculateService;
import com.cooler.semantic.service.WordSegregateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component("sentenceProcessComponent")
public class SentenceProcessComponentImpl extends FunctionComponentBase implements SentenceProcessComponent {

    private static Logger logger = LoggerFactory.getLogger(SentenceProcessComponentImpl.class.getName());

    @Autowired
    private WordSegregateService wordSegregateService;      //分词组件
    @Autowired
    private WeightCalculateService weightCalculateService;  //权重计算组件

    public SentenceProcessComponentImpl() {
        super("SPC", "SO-2", "semanticParserRequest", "sentenceVectors");
    }

    @Override
    protected ComponentBizResult runBiz(DataComponent inputDataComponent) {
        logger.info("SO_2.句子处理");

        SemanticParserRequest request = (SemanticParserRequest)inputDataComponent.getData();
        String cmd = request.getCmd();

        //SO-2-1.多重分词（包含标词）
        logger.info("SO-2-1.分词...");
        Integer accountId = request.getAccountId().get(0);              //取出第一个accountId，进行分词
        List<SentenceVector> sentenceVectors = wordSegregateService.wordSegregate(cmd, accountId, null, Arrays.asList(1), true);

        if (null == sentenceVectors) {
            logger.error("分词错误，原始句子: " + cmd);
            return new ComponentBizResult("SPC_E", IOType.NO_IN_OUT, null);
        }

        //SO-2-1.计算权重
        logger.info("SO-2-2.计算权重...");
        weightCalculateService.calculateWeight(sentenceVectors);          //此方法为分词段设置权重，并返回权重数组，如果分词不成功，则返回默认分词权重数组(此为句子内部各个此的权重值，可以作为后续相似度计算标准)

        DataComponent outputDataComponent = new DataComponentBase("semanticParserRequest", "SemanticParserRequest", sentenceVectors);
        return new ComponentBizResult("SPC_S", IOType.IN_OUT, outputDataComponent);
    }
}
