package com.vcredit.vmc.rules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * </p>
 *
 * @author cailin
 * @since 2020/3/5
 */
@Getter
@AllArgsConstructor
public enum YesOrNoEnum {
    /**
     * 是
     */
    YES(1, "是"),
    NO(0, "否");

    /**
     * code
     */
    private Integer code;

    /**
     * name
     */
    private String name;
}
