package com.vcredit.vmc.rules.fact;

import lombok.Data;

import java.util.List;

/**
 * @author wjp
 * @date 2020/3/10 17:31
 */
@Data
public class TaskNoGetLabelAndPlanFact {
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 随机生成的末尾标签
     */
    private String endLabelChar;
    /**
     * 老的营销标签
     */
    private String oldTaskLabel;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 新的营销标签
     */
    private String promoteTasksLabel;
    /**
     * 营销计划名称
     */
    private List<String> planNames;
    /**
     * 是否传营销标签
     */
    private Boolean isSendTaskLabel;
}
