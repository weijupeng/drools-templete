package com.wjpspace.droolstemplete.entity;

import lombok.Data;

/**
 * @author wjp
 * @date 2020/3/3 11:36
 */
@Data
public class PromotionTaskRuleInput {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 任务描述
     */
    private String taskDesc;
    /**
     * 产品类型
     */
    private String productCode;
    /**
     * 来源
     */
    private Integer promoteSource;
    /**
     * 优先级
     */
    private Integer taskSort;
    /**
     * 任务启动时间
     */
    private String startTime;
    /**
     * 任务关闭时间
     */
    private String endTime;
    /**
     * 是否有效 0否 1是
     */
    private Integer isValid;
    /**
     * 规则类型 0无 1代码配置 2规则文件配置
     */
    private Integer ruleType;
    /**
     * 规则容器ID
     */
    private String kieContainerId;
    /**
     * 规则引擎ID
     */
    private String kieSessionId;
    private String mobile;
    private String eventType;

}
