package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.RRuleEntityMapper;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.internal.RRuleEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RRuleEntityService")
public class RRuleEntityServiceImpl implements RRuleEntityService {
    @Autowired
    private RRuleEntityMapper rRuleEntityMapper;

    @Override
    public List<RRuleEntity> selectBySVRuleInfos(Integer accountId, List<SVRuleInfo> svRuleInfos) {
        return rRuleEntityMapper.selectBySVRuleInfos(accountId, svRuleInfos);
    }

    @Override
    public List<RRuleEntity> selectNecessaryByAIdRId(Integer accountId, Integer enableReferRuleId) {
        return rRuleEntityMapper.selectNecessaryByAIdRId(accountId, enableReferRuleId);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return rRuleEntityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(RRuleEntity record) {
        return rRuleEntityMapper.insert(record);
    }

    @Override
    public int insertSelective(RRuleEntity record) {
        return rRuleEntityMapper.insertSelective(record);
    }

    @Override
    public RRuleEntity selectByPrimaryKey(Integer id) {
        return rRuleEntityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RRuleEntity record) {
        return rRuleEntityMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RRuleEntity record) {
        return rRuleEntityMapper.updateByPrimaryKey(record);
    }

}
