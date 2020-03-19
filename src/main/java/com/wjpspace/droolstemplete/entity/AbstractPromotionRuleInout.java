package com.wjpspace.droolstemplete.entity;

import lombok.Data;

/**
 * @author wjp
 * @date 2020/3/19 10:56
 */
@Data
abstract class AbstractPromotionRuleInout {
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划描述
     */
    private String planDesc;

    /**
     * 规则类型 0 无 1代码配置 2 规则文件配置
     */
    private Integer ruleType;
    /**
     * 规则容器ID
     */
    private String kieContainerId;
    /**
     * 规则文件ID
     */
    private String kieSessionId;

}
