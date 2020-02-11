package com.wjpspace.droolstemplete.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjp
 * @date 2020/1/19 14:48
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 用户生命周期
     */
    REGISTERED("登陆", 1),
    INFORMATION("填写资料", 2),
    CREDIT("授信", 3),
    WITHDRAWALS("提现", 4);


    private String key;
    private Integer value;
}
