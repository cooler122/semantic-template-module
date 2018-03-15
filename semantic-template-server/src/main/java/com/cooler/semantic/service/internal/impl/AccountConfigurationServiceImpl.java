package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.AccountConfigurationMapper;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.service.internal.AccountConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountConfigurationService")
public class AccountConfigurationServiceImpl implements AccountConfigurationService {

    @Autowired
    private AccountConfigurationMapper accountConfigurationMapper;


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return accountConfigurationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountConfiguration record) {
        return accountConfigurationMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountConfiguration record) {
        return accountConfigurationMapper.insertSelective(record);
    }

    @Override
    public AccountConfiguration selectByPrimaryKey(Integer id) {
        return accountConfigurationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountConfiguration record) {
        return accountConfigurationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountConfiguration record) {
        return accountConfigurationMapper.updateByPrimaryKey(record);
    }

    @Override
    public AccountConfiguration selectAIdUId(Integer coreAccountId, Integer userId) {
        return accountConfigurationMapper.selectAIdUId(coreAccountId, userId);
    }
}
