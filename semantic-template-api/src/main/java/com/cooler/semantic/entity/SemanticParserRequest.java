package com.cooler.semantic.entity;

import java.util.List;

public class SemanticParserRequest {

    //--------------------------------------------------------------------0.传入的句子
    /**
     * 请求信息（必须）
     */
    private String cmd;

    //--------------------------------------------------------------------1.账户参数
    /**
     * accountIds（账号列表，为了扩展查询匹配范围，可以传入多个账号）
     */
    private List<Integer> accountId;

    //--------------------------------------------------------------------2.当前场景环境值
    /**
     * 当前所处的场景Id
     */
    private Integer sceneId;

    /**
     * 当前所处的意图ID
     */
    private Integer intentId;

    /**
     * 当前所处的规则ID
     */
    private Integer ruleId;

    //--------------------------------------------------------------------3.配置参数
    /**
     * 是否能跳出上下文
     */
    private boolean canBreakContext = true;

    /**
     * 实体询问最大次数
     */
    private int entityMaxQueryCount = 3;

    /**
     * 规则询问最大次数（要不要看后面效果!!!!!!!!!!!!!!!!!!!!）
     */
    private int ruleMaxQueryCount = 20;

    /**
     * 上下文两次请求的间隔时间（默认一分钟）
     */
    private int contextWaitTime = 60000;

    /**
     * 准确度阈值，设置后，低于这个准确度的rule不会被选中。（默认0.7）
     */
    private double accuracyThreshold  = 0.7;

    //---------------------------------------以下是上次匹配产生的上下文参数（如果丢失，则相当于系统“失忆”，此方案是将上下文数据放在服务端的一种实施方案）
    /**
     * 上下文编号（用来支持上下文的字段）
     */
    private String contextId;

    /**
     * 上次询问的实体的实体ID
     */
    private Integer queryEntityId;

    /**
     * 实体ID已经积累下来的询问次数（用来支持限定次数）
     */
    private int accumulatedQueryCount;

    /**
     * 上次请求的结束时间戳（用来支持限定时间）
     */
    private long lastResponseTimestamp;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<Integer> getAccountId() {
        return accountId;
    }

    public void setAccountId(List<Integer> accountId) {
        this.accountId = accountId;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getIntentId() {
        return intentId;
    }

    public void setIntentId(Integer intentId) {
        this.intentId = intentId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public boolean isCanBreakContext() {
        return canBreakContext;
    }

    public void setCanBreakContext(boolean canBreakContext) {
        this.canBreakContext = canBreakContext;
    }

    public int getEntityMaxQueryCount() {
        return entityMaxQueryCount;
    }

    public void setEntityMaxQueryCount(int entityMaxQueryCount) {
        this.entityMaxQueryCount = entityMaxQueryCount;
    }

    public int getRuleMaxQueryCount() {
        return ruleMaxQueryCount;
    }

    public void setRuleMaxQueryCount(int ruleMaxQueryCount) {
        this.ruleMaxQueryCount = ruleMaxQueryCount;
    }

    public int getContextWaitTime() {
        return contextWaitTime;
    }

    public void setContextWaitTime(int contextWaitTime) {
        this.contextWaitTime = contextWaitTime;
    }

    public double getAccuracyThreshold() {
        return accuracyThreshold;
    }

    public void setAccuracyThreshold(double accuracyThreshold) {
        this.accuracyThreshold = accuracyThreshold;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public Integer getQueryEntityId() {
        return queryEntityId;
    }

    public void setQueryEntityId(Integer queryEntityId) {
        this.queryEntityId = queryEntityId;
    }

    public int getAccumulatedQueryCount() {
        return accumulatedQueryCount;
    }

    public void setAccumulatedQueryCount(int accumulatedQueryCount) {
        this.accumulatedQueryCount = accumulatedQueryCount;
    }

    public long getLastResponseTimestamp() {
        return lastResponseTimestamp;
    }

    public void setLastResponseTimestamp(long lastResponseTimestamp) {
        this.lastResponseTimestamp = lastResponseTimestamp;
    }
}
