package com.wjpspace.droolstemplete.entity;

import lombok.Data;

/**
 * <p>
 * TelemarketingPromotionRuleOutput
 * <p>
 *
 * @author: kancy
 * @date: 2020/3/2 17:19
 **/
@Data
public class TelemarketingPromotionRuleOutput {
    /**
     * 结果
     */
    private Boolean result;
    /**
     * 拒绝原因码
     */
    private String rejectCode;
    /**
     * 拒绝原因
     */
    private String rejectReason;
}
