package com.cooler.semantic.service.internal;

import com.cooler.semantic.entity.REntityWord;

import java.util.List;
import java.util.Set;

public interface REntityWordService extends BaseService<REntityWord> {
    /**
     * 根据accountId和词语字符串集合查询REntityWord集合(这个查询在sql里面会将系统账户（accountId=1）自动放到里面，详见REntityWordMapper.xml的selectByAIdWords）
     * @param accountId
     * @param words
     * @return
     */
    List<REntityWord> selectByAIdWords(Integer accountId, Set<String> words);
}
