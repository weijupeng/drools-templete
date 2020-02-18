package com.wjpspace.droolstemplete.service;

import com.wjpspace.droolstemplete.entity.Customer;
import com.wjpspace.droolstemplete.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    List<Person> changeCustomerStep(List<Customer> customers) {

        if (CollectionUtils.isEmpty(customers)) {
            System.out.println("无数据");
            return null;
        }

        List<Person> collect = customers.stream().map(c -> {
            Person person = new Person();
            BeanUtils.copyProperties(c, person);
            person.setStep(c.getStatus());
            int i = new Random().nextInt(2) + 1;
            //判断当前是否可以修改
            if (i == 1) {
                person.setCurrentStep(person.getStep());
            } else {
                person.setCurrentStep(c.getStatus() + 1);
                c.setStatus(c.getStatus() + 1);
            }
            log.info("person最终态为:{}", person);
            return person;
        }).collect(Collectors.toList());
        return collect;
    }
}
