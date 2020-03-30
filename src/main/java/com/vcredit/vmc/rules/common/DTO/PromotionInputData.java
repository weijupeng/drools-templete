package com.vcredit.vmc.rules.common.DTO;

import lombok.Data;

/**
 * <p>
 * PromotionInputData
 * <p>
 **/
@Data
public class PromotionInputData {
    /**
     * 发起ID
     */
    private Long originalId;
    /**
     * 产品代码
     *
     */
    private String productCode;
    /**
     * 当前步骤
     *
     */
    private String step;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 来源
     *
     */
    private Integer promoteSource;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * pid
     */
    private String pid;
    /**
     * 注册ID
     */
    private String registerId;
    /**
     * 授信流水号
     */
    private String creditSerialNo;
    /**
     * 订单类型 （1-首贷，2-加贷，3-再贷，4-首贷非首笔）
     */
    private Integer orderType;
    private String kieContainerId;
    private String kieSessionId;
}
