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

    /**
     * 根据账号和ruleId来查询RRE集合
     * @param accountId
     * @param enableReferRuleId
     * @return
     */
    List<RRuleEntity> selectNecessaryByAIdRId(Integer accountId, Integer enableReferRuleId);

    /**
     * 根据所有ruleIds来查询相关的RRE集合（必须实体）
     * @param accountId
     * @param referRuleIds
     * @return
     */
    List<RRuleEntity> selectByRuleIds(Integer accountId, List<Integer> referRuleIds);
}
