package com.cooler.semantic.service.internal;

import com.cooler.semantic.entity.WordCN;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

public interface WordCNService extends BaseService<WordCN>{
    /**
     * 按照字符串词集来查询WordCN对象
     * @param accountId 次词语插入者账户
     * @param words  字符串词集
     * @return
     */
    List<WordCN> selectByWords(Integer accountId, @Param("words") Set<String> words);
}
