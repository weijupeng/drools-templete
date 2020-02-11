package com.wjpspace.droolstemplete.task;

import com.wjpspace.droolstemplete.common.enums.BusinessStepEnum;
import com.wjpspace.droolstemplete.dao.CustomerDao;
import com.wjpspace.droolstemplete.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author wjp
 * @date 2020/1/20 15:22
 */
@Configuration
@EnableScheduling
@Slf4j
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class UserStatusTask {
    @Resource
    private CustomerDao customerDao;

    /**
     * 未注册用户
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void setDrl0() {
        changeCustomers(0);
        log.info("未注册用户定时任务执行了一次");
    }


    @Scheduled(cron = "0/5 * * * * ?")
    public void setDrl1() {
        Integer value = BusinessStepEnum.REGISTER.getValue();
        changeCustomers(value);
        log.info("注册用户定时任务执行了一次");
    }

    @Scheduled(cron = "0/6 * * * * ?")
    public void setCredit1() {
        Integer value = BusinessStepEnum.CREDIT_1.getValue();
        changeCustomers(value);
        log.info("授信第一阶段定时任务执行了一次");
    }

    @Scheduled(cron = "0/6 * * * * ?")
    public void setCredit2() {
        Integer value = BusinessStepEnum.CREDIT_2.getValue();
        changeCustomers(value);
        log.info("授信第二阶段定时任务执行了一次");
    }

    @Scheduled(cron = "0/6 * * * * ?")
    public void setCredit3() {
        Integer value = BusinessStepEnum.CREDIT_3.getValue();
        changeCustomers(value);
        log.info("授信第三阶段定时任务执行了一次");
    }

    @Scheduled(cron = "0/6 * * * * ?")
    public void setCredit4() {
        Integer value = BusinessStepEnum.CREDIT_4.getValue();
        changeCustomers(value);
        log.info("授信第四阶段定时任务执行了一次");
    }


    @Scheduled(cron = "0/7 * * * * ?")
    public void setDecisionApply() {
        Integer value = BusinessStepEnum.DECISION_APPLY.getValue();
        changeCustomers(value);
        log.info("进入决策定时任务执行了一次");
    }

    @Scheduled(cron = "0/7 * * * * ?")
    public void setDecisionComplete() {
        Integer value = BusinessStepEnum.DECISION_COMPLETE.getValue();
        changeCustomers(value);
        log.info("决策出参定时任务执行了一次");
    }

    /**
     * 数据库读取用户状态并改变
     * @param value 用户类型
     * @return
     */
    private void changeCustomers(Integer value) {
        List<Customer> customers = customerDao.getCustomerList(value);
        if (CollectionUtils.isEmpty(customers)) {
            System.out.println(value + "无数据");
            return;
        }
        List<Customer> collect = customers.stream().map(c -> {
            int i = new Random().nextInt(2) + 1;
            //判断当前是否可以修改
            System.out.println("i = " + i);
            //屏蔽终结步骤
            boolean dontFinish = !c.getStatus().equals(BusinessStepEnum.REFUSED.getValue());
            boolean notRefused = !c.getStatus().equals(BusinessStepEnum.APPLY_ORDER_1.getValue());
            boolean dontNeedNext = dontFinish || notRefused;
            if (dontNeedNext && i == 1) {
                if (new Random().nextInt(9) + 1 == 1) {
                    c.setStatus(BusinessStepEnum.REFUSED.getValue());
                } else {
                    c.setStatus(c.getStatus() + 1);
                }
            }
            return c;
        }).collect(Collectors.toList());
        customerDao.saveOrUpdateBatch(collect);
    }
}
