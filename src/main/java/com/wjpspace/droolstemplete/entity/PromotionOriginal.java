package com.wjpspace.droolstemplete.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author weijupeng 营销发起记录表
 */
@Data
@TableName("t_promotion_original")
public class PromotionOriginal {
    @TableId
    private Long id;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户中心注册ID
     */
    private Long registerId;
    /**
     * 产品中心业务ID
     */
    private Long businessId;
    /**
     * 来源 流式/t+12
     */
    private Integer promoteSource;
    /**
     * 营销任务ID
     */
    private String promoteTasksNo;
    /**
     * 营销标签
     */
    private String promoteTasksLabel;
    /**
     * 前置标签（备用）
     */
    private String frontLabel;
    /**
     * 创建时间
     */
    private String createdTime;
    /**
     * 更新时间
     */
    private String updatedTime;
}