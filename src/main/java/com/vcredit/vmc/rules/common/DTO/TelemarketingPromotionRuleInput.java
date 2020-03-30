package com.vcredit.vmc.rules.common.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * TelemarketingPromotionRuleInput
 * <p>
 *
 * @author: kancy
 * @date: 2020/3/2 17:19
 **/
@Data
public class TelemarketingPromotionRuleInput extends AbstractPromotionRuleInout {
    /**
     * 产品代码
     */
    private String productCode;
    /**
     * 来源
     */
    private Integer promoteSource;
    /**
     * 当前是否完成
     */
    private String step;

    /**
     * 当前是否完成
     */
    private String currentStep;
    /**
     * 身份证
     */
    private String idNo;
    /**
     * 额度
     * 保留两位小数
     */
    private BigDecimal quota;
    /**
     * 数据来源
     */
    private String dataSources;
    /**
     * 前置费用收取标签 Y/N
     */
    private String foreFeeRecvTag;
    /**
     * 营销计划ID
     */
    private Long planId;
}
