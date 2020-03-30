package com.vcredit.vmc.rules.common.enums;

import java.util.Objects;

/**
 * <p>
 * BusinessStepEnum
 * <p>
 **/

public enum BusinessStepEnum {
    /**
     * 注册
     */
    REGISTER,
    /**
     * 授信第一步
     */
    APPLYAUTH_1,
    /**
     * 授信第二步
     */
    APPLYAUTH_2,
    /**
     * 授信第三步
     */
    APPLYAUTH_3,
    /**
     * 授信第四步
     */
    APPLYAUTH_4,
    /**
     * 进入决策
     */
    DECISION_APPLY,
    /**
     * 决策出额（包含首再加）
     */
    DECISION_COMPILTE,
    /**
     * 提现完成
     */
    APPLY_ORDER_1,
    /**
     * 最终拒绝状态，不推电销
     */
    REFUSED;

    /**
     * 是否处于授信阶段
     *
     * @param step 当前步骤
     *
     * @return 是否
     */
    public static boolean onGrantCredit(BusinessStepEnum step) {
        return BusinessStepEnum.APPLYAUTH_1.equals(step)
                || BusinessStepEnum.APPLYAUTH_2.equals(step) || BusinessStepEnum.APPLYAUTH_3.equals(step)
                || BusinessStepEnum.APPLYAUTH_4.equals(step);
    }

    /**
     * 判断当前步骤是否是最终步骤
     *
     * @param step 当前步骤
     *
     * @return 是否
     */
    public static boolean isFinished(BusinessStepEnum step) {
        return BusinessStepEnum.APPLY_ORDER_1.equals(step) || BusinessStepEnum.REFUSED.equals(step);
    }

}
