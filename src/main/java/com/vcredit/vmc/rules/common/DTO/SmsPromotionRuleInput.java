package com.vcredit.vmc.rules.common.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * </p>
 *
 * @author cailin
 * @since 2020/3/11
 */
@Data
public class SmsPromotionRuleInput extends AbstractPromotionRuleInout{
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
     * 额度 保留两位小数
     */
    private BigDecimal quota;
    /**
     * 授信过期日期 "2019-02-16 05:10:00"
     */
    private String creditExpireTime;
    /**
     * 数据来源
     */
    private String dataSources;
    /**
     * 前置费用收取标签 Y/N
     */
    private String foreFeeRecvTag;
}
