package com.vcredit.vmc.rules.drools.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjp
 * @date 2020/3/10 17:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionLabelAndPlanInput {
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 老的营销标签
     */
    private String oldTaskLabel;
    /**
     * 事件类型
     */
    private String eventType;

}
