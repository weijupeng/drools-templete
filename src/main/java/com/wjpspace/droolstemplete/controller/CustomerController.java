package com.wjpspace.droolstemplete.controller;

import com.wjpspace.droolstemplete.service.CustomerService;
import com.wjpspace.droolstemplete.service.RemoteCustomerService;
import org.springframework.web.bind.annotation.GetMapping;
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
}
