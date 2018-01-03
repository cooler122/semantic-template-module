package com.cooler.semantic.service.internal;

import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.SVRuleInfo;

import java.util.List;

public interface RRuleEntityService extends BaseService<RRuleEntity> {
    /**
     * 根据SVRuleInfo集合参数来查询相关的RRuleEntity集合
     * @param accountId
     * @param svRuleInfos
     * @return
     */
    List<RRuleEntity> selectBySVRuleInfos(Integer accountId, List<SVRuleInfo> svRuleInfos);
}
