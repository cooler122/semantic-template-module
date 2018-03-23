package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.dao.ReferRuleRelationMapper;
import com.cooler.semantic.entity.ReferRuleRelation;
import com.cooler.semantic.service.external.ReferRuleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("referRuleRelationService")
public class ReferRuleRelationServiceImpl implements ReferRuleRelationService {
    @Autowired
    private ReferRuleRelationMapper referRuleRelationMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return referRuleRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ReferRuleRelation record) {
        return referRuleRelationMapper.insert(record);
    }

    @Override
    public int insertSelective(ReferRuleRelation record) {
        return referRuleRelationMapper.insertSelective(record);
    }

    @Override
    public ReferRuleRelation selectByPrimaryKey(Integer id) {
        return referRuleRelationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ReferRuleRelation record) {
        return referRuleRelationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ReferRuleRelation record) {
        return referRuleRelationMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<ReferRuleRelation> selectByRIdAId(Integer ruleId, Integer guideIntentId, Integer accountId) {
        return referRuleRelationMapper.selectByRIdAId(ruleId, guideIntentId, accountId);
    }

    @Override
    public List<Integer> selectRRIdsByIntentId(Integer accountId, Integer guideIntentId) {
        return referRuleRelationMapper.selectRRIdsByIntentId(accountId, guideIntentId);
    }


}
