package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.facade.CustomizedSemanticFacade;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.model.SentenceVectorParam;
import com.cooler.semantic.service.external.WeightCalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component("sentenceProcessComponent")
public class SentenceProcessComponentImpl extends FunctionComponentBase<SemanticParserRequest, List<SentenceVector>> {

    private static Logger logger = LoggerFactory.getLogger(SentenceProcessComponentImpl.class.getName());

    @Autowired
    private CustomizedSemanticFacade customizedSemanticFacade;                                                     //自定义语义组件（包含自定义分词）                                                //指代消解组件
    @Autowired
    private WeightCalculateService weightCalculateService;                                                         //权重计算组件

    public SentenceProcessComponentImpl() {
        super("SPC", "semanticParserRequest", "sentenceVectors");
    }

    @Override
    protected ComponentBizResult<List<SentenceVector>> runBiz(ContextOwner contextOwner, SemanticParserRequest bizData) {
        logger.trace("SPC.句子处理");

        //SO-2-1.多重分词（包含标词）
        Integer accountId = contextOwner.getCoreAccountId();                                                                //取出第一个accountId，进行分词
        String cmd = bizData.getCmd();
        List<SentenceVectorParam> sentenceVectorParams = customizedSemanticFacade.semanticParse(cmd, accountId, Arrays.asList(1), true);

        if (null == sentenceVectorParams || sentenceVectorParams.size() == 0) {
            logger.error("分词错误，原始句子: " + cmd);
            return new ComponentBizResult("SPC_F","SPC_F1_semanticParseError");
        }

        //SO-2-2.计算权重
        List<SentenceVector> sentenceVectors = weightCalculateService.calculateWeight(accountId, sentenceVectorParams);  //此方法为分词段设置权重，并返回权重数组，如果分词不成功，则返回默认分词权重数组(此为句子内部各个此的权重值，可以作为后续相似度计算标准)

        return new ComponentBizResult("SPC_S", Constant.STORE_LOCAL, sentenceVectors);
    }
}
