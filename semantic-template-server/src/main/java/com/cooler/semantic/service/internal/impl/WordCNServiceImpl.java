package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.WordCNMapper;
import com.cooler.semantic.entity.WordCN;
import com.cooler.semantic.service.internal.WordCNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("wordCNService")
public class WordCNServiceImpl implements WordCNService {

    @Autowired
    private WordCNMapper wordCNMapper;


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return wordCNMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WordCN record) {
        return wordCNMapper.insert(record);
    }

    @Override
    public int insertSelective(WordCN record) {
        return wordCNMapper.insertSelective(record);
    }

    @Override
    public WordCN selectByPrimaryKey(Integer id) {
        return wordCNMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WordCN record) {
        return wordCNMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WordCN record) {
        return wordCNMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<WordCN> selectByWords(Set<String> words) {
        return wordCNMapper.selectByWords(words);
    }
}
