package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.WordCNMapper;
import com.cooler.semantic.entity.WordCN;
import com.cooler.semantic.service.internal.WordCNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("wordCNService")
public class WordCNServiceImpl implements WordCNService {

    private static Logger logger = LoggerFactory.getLogger(WordCNServiceImpl.class.getName());

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
    public List<WordCN> selectByWords(Integer accountId, Set<String> words) {
        List<WordCN> db_wordCNs = wordCNMapper.selectByWords(words);                                                    //数据库里面保存的wordCN记录
        for (WordCN db_wordCN : db_wordCNs) {
            words.remove(db_wordCN.getWord());                                                                          //将查询到的词语去除掉，留下数据库中没有记录的词语
        }

        String wordDuplicate = "";
        try{
            for (String word : words) {                                                                                 //将数据库中没有记录的词语添加到数据库word_cn表中
                WordCN wordCN = new WordCN();
                wordCN.setWord(word);
                wordCN.setAccountId(accountId);
                wordDuplicate = word;                                                                                   //这里将字符串传出去，如果重复性插入会报异常，捕捉后会有日志提示具体重复性插入的词语
                int wordId = wordCNMapper.insert(wordCN);
                if(wordId != 0){
                    db_wordCNs.add(wordCN);                                                                             //此时wordCN对象会将主键带回来，但没有带回state和create_time，这两个字段当前没有作用
                }
            }
        }catch (Exception e){
            logger.warn(wordDuplicate + " 字符串重复性插入！\n" + e.getMessage());
        }

        return db_wordCNs;
    }
}
