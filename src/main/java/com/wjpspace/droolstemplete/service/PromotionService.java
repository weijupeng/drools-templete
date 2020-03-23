package com.wjpspace.droolstemplete.service;

import com.wjpspace.droolstemplete.entity.PromotionTaskRuleInput;
import com.wjpspace.droolstemplete.entity.TelemarketingPromotionRuleInput;
import com.wjpspace.droolstemplete.entity.TelemarketingPromotionRuleOutput;

/**
 * @author wjp
 * @date 2020/3/16 14:52
 */
public interface PromotionService {

    void stream(PromotionTaskRuleInput input);

    void rule(PromotionTaskRuleInput input);

    TelemarketingPromotionRuleOutput rule2(TelemarketingPromotionRuleInput input);

    TelemarketingPromotionRuleOutput rule3(TelemarketingPromotionRuleInput input);
}
