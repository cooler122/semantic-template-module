package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.AccountMapper;
import com.cooler.semantic.entity.Account;
import com.cooler.semantic.service.internal.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountService")
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return accountMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Account record) {
        return accountMapper.insert(record);
    }

    @Override
    public int insertSelective(Account record) {
        return accountMapper.insertSelective(record);
    }

    @Override
    public Account selectByPrimaryKey(Integer id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Account record) {
        return accountMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Account record) {
        return accountMapper.updateByPrimaryKey(record);
    }
}
