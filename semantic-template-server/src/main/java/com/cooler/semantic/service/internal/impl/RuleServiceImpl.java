package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.RuleMapper;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.service.internal.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
