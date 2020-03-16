package com.vcredit.vmc.rules.output;

import lombok.Data;

import java.util.List;

/**
 * @author wjp
 * @date 2020/3/16 15:58
 */
@Data
public class PromotionTaskRuleOutput {
    /**
     * 营销计划名称集合
     */
    private List<String> planNames;
    /**
     * 营销标签
     */
    private String promoteTasksLabel;
    /**
     * 是否传营销标签
     */
    private Boolean isSendTaskLabel;
}
