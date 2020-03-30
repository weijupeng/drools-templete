package com.vcredit.vmc.rules.drools.output;

import lombok.Data;

import java.util.List;

/**
 * @author wjp
 * @date 2020/3/17 17:07
 */
@Data
public class PromotionLabelAndPlanOutput {
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
