package com.cooler.semantic.service.internal;

import com.cooler.semantic.entity.WordCN;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

public interface WordCNService extends BaseService<WordCN>{
    /**
     * 按照字符串词集来查询WordCN对象
     * @param words  字符串词集
     * @return
     */
    List<WordCN> selectByWords(@Param("words") Set<String> words);
}
