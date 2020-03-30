package com.vcredit.vmc.rules.drools.output;


import lombok.Data;

/**
 * <p>
 * </p>
 * @author cailin
 * @since 2020/3/10
 */
@Data
public class SmsAntiCheckOutput {
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
