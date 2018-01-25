package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.RuleMapper;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.service.internal.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {
    @Autowired
    private RuleMapper ruleMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return ruleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Rule record) {
        return ruleMapper.insert(record);
    }

    @Override
    public int insertSelective(Rule record) {
        return ruleMapper.insertSelective(record);
    }

    @Override
    public Rule selectByPrimaryKey(Integer id) {
        return ruleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Rule record) {
        return ruleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Rule record) {
        return ruleMapper.updateByPrimaryKey(record);
    }

    @Override
    public Double selectAccuracyThresholdByIdAccountId(Integer accountId, Integer ruleId) {
        Double ruleAccuracyThreshold = ruleMapper.selectAccuracyThresholdByIdAccountId(accountId, ruleId);                        //最多取出两个记录
        return ruleAccuracyThreshold;
    }
}
