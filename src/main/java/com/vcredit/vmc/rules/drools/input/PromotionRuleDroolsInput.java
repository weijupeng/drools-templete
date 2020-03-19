package com.vcredit.vmc.rules.drools.input;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author weijupeng
 */
@Data
public class PromotionRuleDroolsInput {

    /**
     * 产品代码
     */
    private String productCode;
    /**
     * 来源
     */
    private Integer promoteSource;
    /**
     * 入参步骤
     */
    private String step;
    /**
     * 当前步骤
     */
    private String currentStep;
    /**
     * 入参步骤字符
     */
    private Integer stepNumber;
    /**
     * 当前步骤字符
     */
    private Integer currentStepNumber;
    /**
     * 额度 保留两位小数
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
     * 身份证
     */
    private String idNo;


}