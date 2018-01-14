package com.cooler.semantic.service.external;

import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AnaphoraResolutionService {

    /**
     * 指代消解
     * @param contextOwner      账户ID
     * @param words  被测试词语
     * @return  指代消解后的新句子向量
     */
    Map<String, List<REntityWordInfo>> anaphoraResolution(ContextOwner contextOwner, Set<String> words);
}
