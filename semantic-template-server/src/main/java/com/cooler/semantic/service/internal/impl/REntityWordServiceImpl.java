package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.REntityWordMapper;
import com.cooler.semantic.entity.REntityWord;
import com.cooler.semantic.service.internal.REntityWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("rEntityWordService")
public class REntityWordServiceImpl implements REntityWordService {

    @Autowired
    private REntityWordMapper rEntityWordMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return rEntityWordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(REntityWord record) {
        return rEntityWordMapper.insert(record);
    }

    @Override
    public int insertSelective(REntityWord record) {
        return rEntityWordMapper.insertSelective(record);
    }

    @Override
    public REntityWord selectByPrimaryKey(Integer id) {
        return rEntityWordMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(REntityWord record) {
        return rEntityWordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(REntityWord record) {
        return rEntityWordMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<REntityWord> selectByAIdWords(Integer accountId, Set<String> allWords) {
        return rEntityWordMapper.selectByAIdWords(accountId, allWords);
    }
}
