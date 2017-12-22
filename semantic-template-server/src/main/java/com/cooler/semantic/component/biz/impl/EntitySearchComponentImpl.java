package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.biz.EntitySearchComponent;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.model.SentenceVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("entitySearchComponent")
public class EntitySearchComponentImpl extends FunctionComponentBase<List<SentenceVector>, List<SentenceVector>> implements EntitySearchComponent {

    private static Logger logger = LoggerFactory.getLogger(EntitySearchComponentImpl.class.getName());

    public EntitySearchComponentImpl() {
        super("ESC", "SO-3", "sentenceVectors", "sentenceVectors");
    }

    @Override
    protected ComponentBizResult<List<SentenceVector>> runBiz(List<SentenceVector> bizData) {
        logger.info("SO_3.词语-实体检索");

        //TODO:词语-实体检索
        List<SentenceVector> sentenceVectors = null;            //获取这个量

        return new ComponentBizResult("ESC_S", true, sentenceVectors);
    }


}
