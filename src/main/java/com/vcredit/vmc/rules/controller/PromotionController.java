package com.vcredit.vmc.rules.controller;

import com.vcredit.vmc.rules.common.DTO.PromotionInputData;
import com.vcredit.vmc.rules.common.DTO.SmsPromotionRuleInput;
import com.vcredit.vmc.rules.common.DTO.TelemarketingPromotionRuleInput;
import com.vcredit.vmc.rules.drools.output.PromotionLabelAndPlanOutput;
import com.vcredit.vmc.rules.drools.output.SmsAntiCheckOutput;
import com.vcredit.vmc.rules.drools.output.TeleAntiCheckOutput;
import com.vcredit.vmc.rules.service.PromotionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wjp
 * @date 2020/3/27 18:52
 */
@RestController
public class PromotionController {
    @Resource
    private PromotionService promotionService;

    @PostMapping("promotion/check")
    public PromotionLabelAndPlanOutput check1(@RequestBody PromotionInputData input) {
        return promotionService.check1(input);
    }


    @PostMapping("promotion/check2")
    public SmsAntiCheckOutput check2(@RequestBody SmsPromotionRuleInput input) {
        return promotionService.check2(input);

    }

    @PostMapping("promotion/check3")
    public TeleAntiCheckOutput check3(@RequestBody TelemarketingPromotionRuleInput input) {
        return promotionService.check3(input);
    }
}
