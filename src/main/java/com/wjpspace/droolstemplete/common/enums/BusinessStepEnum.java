package com.wjpspace.droolstemplete.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Description: 业务步骤
 * ****下面的位置千万不要动，因为代码中有应用compare方法****
 * @author niumangyuan
 * @version v1.0
 * @date 2020/1/6
 **/
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum BusinessStepEnum {
    /**
     * 注册
     */
    REGISTER("register", 1),
    /**
     * 授信第一步
     */
    CREDIT_1("CREDIT_1", 2),
    /**
     * 授信第二步
     */
    CREDIT_2("CREDIT_2", 3),
    /**
     * 授信第三步
     */
    CREDIT_3("CREDIT_3", 4),
    /**
     * 授信第四步
     */
    CREDIT_4("CREDIT_4", 5),
    /**
     * 进入决策
     */
    DECISION_APPLY("DECISION_APPLY", 6),
    /**
     * 决策出额（包含首再加）
     */
    DECISION_COMPLETE("DECISION_COMPLETE", 7),
    /**
     * 提现完成
     */
    APPLY_ORDER_1("APPLY_ORDER_1", 8),
    /**
     * 最终拒绝状态，不推电销
     */
    REFUSED("REFUSED", 9);


    private String key;
    private Integer value;

    /**
     * 是否处于授信阶段
     * @param step 当前步骤
     * @return 是否
     */
    public static boolean onGrantCredit(BusinessStepEnum step) {
        return BusinessStepEnum.CREDIT_1.equals(step)
                || BusinessStepEnum.CREDIT_2.equals(step) || BusinessStepEnum.CREDIT_3.equals(step)
                || BusinessStepEnum.CREDIT_4.equals(step);
    }

    /**
     * 判断当前步骤是否是最终步骤
     * @param step 当前步骤
     * @return 是否
     */
    public static boolean isFinished(BusinessStepEnum step) {
        return BusinessStepEnum.APPLY_ORDER_1.equals(step) || BusinessStepEnum.REFUSED.equals(step);
    }

    /**
     * 不需要接收的步骤
     * @param step 当前步骤
     * @return 是否
     */
    public static boolean dontNeedReceived(BusinessStepEnum step) {
        // 不需要接收的
        // 完成步骤
        boolean finished = isFinished(step);
        // 在授信，但是不是第一步
        boolean onGrantCreditAndNotStepOne = onGrantCredit(step) && !CREDIT_1.equals(step);
        return finished || onGrantCreditAndNotStepOne;
    }


}
