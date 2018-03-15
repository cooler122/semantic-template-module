package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.dao.ReferRuleConditionMapper;
import com.cooler.semantic.entity.ReferRuleCondition;
import com.cooler.semantic.service.external.ReferRuleConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("referRuleConditionService")
public class ReferRuleConditionServiceImpl implements ReferRuleConditionService {

    @Autowired
    private ReferRuleConditionMapper referRuleConditionMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return referRuleConditionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ReferRuleCondition record) {
        return referRuleConditionMapper.insert(record);
    }

    @Override
    public int insertSelective(ReferRuleCondition record) {
        return referRuleConditionMapper.insertSelective(record);
    }

    @Override
    public ReferRuleCondition selectByPrimaryKey(Integer id) {
        return referRuleConditionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ReferRuleCondition record) {
        return referRuleConditionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ReferRuleCondition record) {
        return referRuleConditionMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<ReferRuleCondition> selectByRRRelationId(Integer referRuleRelationId) {
        return referRuleConditionMapper.selectByRRRelationId(referRuleRelationId);
    }
}
