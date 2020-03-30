package com.vcredit.vmc.rules.service;

import com.vcredit.vmc.rules.common.DTO.PromotionInputData;
import com.vcredit.vmc.rules.common.DTO.SmsPromotionRuleInput;
import com.vcredit.vmc.rules.common.DTO.TelemarketingPromotionRuleInput;
import com.vcredit.vmc.rules.drools.output.PromotionLabelAndPlanOutput;
import com.vcredit.vmc.rules.drools.output.SmsAntiCheckOutput;
import com.vcredit.vmc.rules.drools.output.TeleAntiCheckOutput;

/**
 * @author wjp
 * @date 2020/3/27 18:52
 */
public interface PromotionService {

    PromotionLabelAndPlanOutput check1(PromotionInputData input);

    SmsAntiCheckOutput check2(SmsPromotionRuleInput input);

    TeleAntiCheckOutput check3(TelemarketingPromotionRuleInput input);
}
