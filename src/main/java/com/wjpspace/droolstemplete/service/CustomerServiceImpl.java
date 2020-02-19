package com.wjpspace.droolstemplete.service;

import com.wjpspace.droolstemplete.dao.CustomerDao;
import com.wjpspace.droolstemplete.entity.Customer;
import com.wjpspace.droolstemplete.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wjp
 * @date 2020/1/20 16:09
 */
@Service
@Slf4j
public class CustomerServiceImpl extends AbstractService implements CustomerService {
    @Resource
    private CustomerDao customerDao;


    /**
     * 执行本地规则文件
     */
    @Override
    public void go() {
        ArrayList<Customer> customers = new ArrayList<>();
        //获取原始数据
        List<Customer> customerList = customerDao.getAllCustomers();

        //获取规则1的kieSession
        KieServices kieServices = KieServices.Factory.get();
        KieSession kieSession = kieServices.getKieClasspathContainer().newKieSession("ksession-rules");

        //遍历原始数据匹配规则
        for (Customer customer : customerList) {
            FactType factType = kieSession.getKieBase().getFactType("rules", "Person");
            Object person = null;
            try {
                person = factType.newInstance();
                factType.set(person, "step", customer.getStatus());
                factType.set(person, "id", customer.getId());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }


            //转换成数据实体
//            Person person = new Person();
//            BeanUtils.copyProperties(customer, person);
//            person.setStep(customer.getStatus());

            //执行规则并拿到结果
            kieSession.insert(person);
            kieSession.fireAllRules();

            //符合规则1的加入到集合，准备进行第二遍规则匹配

            if ((boolean)(factType.get(person, "valid"))){
                Object id = factType.get(person, "id");
                Customer c = customerList.stream().filter(customer1 -> customer1.getId().equals((Long)id)).collect(Collectors.toList()).get(0);
                customers.add(c);
            }
        }

        //进行第二遍规则匹配
        sendMessage(customers);
    }

    private void sendMessage(List<Customer> customers) {
        //随机对数据进行改变
        List<Person> personList = changeCustomerStep(customers);
        //
        if (CollectionUtils.isEmpty(personList)) {
            System.out.println("当前无数据可做规则校验修改");
            throw new RuntimeException("当前无数据可做规则校验修改");
        }
        ArrayList<Long> idList = new ArrayList<>();

        //获取第二个规则文件的kieSession
        KieServices kieServices = KieServices.Factory.get();
        KieSession kieSession = kieServices.getKieClasspathContainer().newKieSession("ksession-rules2");

        //遍历数据对象并对匹配规则的数据获取ID加入到集合
        personList.forEach(person -> {
            kieSession.insert(person);
            kieSession.fireAllRules();
            if (person.getValid()) {
                idList.add(person.getId());
            }
        });
        System.out.println("需要被推送到电销系统的是：");
        System.out.println(idList);
    }


}
