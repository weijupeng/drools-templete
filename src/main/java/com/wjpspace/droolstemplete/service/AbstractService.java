package com.wjpspace.droolstemplete.service;

import com.wjpspace.droolstemplete.common.enums.BusinessStepEnum;
import com.wjpspace.droolstemplete.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author wjp
 * @date 2020/2/18 14:52
 */
@Slf4j
public abstract class AbstractService {

    private final static Integer TWENTY = 20;

    List<Person> changeCustomerStep(List<Person> people) {

        if (CollectionUtils.isEmpty(people)) {
            System.out.println("无数据");
            return null;
        }

        return people.stream().peek(c -> {
            int i = new Random().nextInt(2) + 1;
            //判断当前是否可以修改
            if (i == 1) {
                c.setCurrentStep(c.getStep());
            } else if (new Random().nextInt(TWENTY) + 1 == 1) {
                c.setCurrentStep(BusinessStepEnum.REFUSED.getValue());
            } else {
                c.setCurrentStep(c.getStep() + 1);
            }
            log.info("person最终态为:{}", c);
        }).collect(Collectors.toList());
    }
}
