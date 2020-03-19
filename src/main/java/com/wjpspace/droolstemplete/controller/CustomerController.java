package com.wjpspace.droolstemplete.controller;

import com.wjpspace.droolstemplete.entity.PromotionTaskRuleInput;
import com.wjpspace.droolstemplete.entity.TelemarketingPromotionRuleInput;
import com.wjpspace.droolstemplete.entity.TelemarketingPromotionRuleOutput;
import com.wjpspace.droolstemplete.service.CustomerService;
import com.wjpspace.droolstemplete.service.PromotionService;
import com.wjpspace.droolstemplete.service.RemoteCustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wjp
 * @date 2020/1/21 18:06
 */
@RestController
public class CustomerController {
    @Resource
    private CustomerService customerService;

    @Resource
    private RemoteCustomerService remoteCustomerService;

    @Resource
    private PromotionService promotionService;

    /**
     * 本地规则文件
     */
    @GetMapping("/go")
    public String go() {
        try {
            customerService.go();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "Success";
    }

    /**
     * 远程规则文件
     */
    @GetMapping("/go2")
    public String go2() {
        try {
            remoteCustomerService.go2();
        } catch (Exception e) {
             e.printStackTrace();
            return "false";
        }
        return "Success";
    }


    @PostMapping("/stream")
    public String stream(@RequestBody PromotionTaskRuleInput input) {
        try {
            promotionService.stream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "true";
    }

    @PostMapping("/rule")
    public String rule(@RequestBody PromotionTaskRuleInput input) {
        try {
            promotionService.rule(input);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "true";
    }

    @PostMapping("/rule2")
    public TelemarketingPromotionRuleOutput rule2(@RequestBody TelemarketingPromotionRuleInput input) {
       return promotionService.rule2(input);
    }

}
