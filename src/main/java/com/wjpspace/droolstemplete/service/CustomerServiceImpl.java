package com.wjpspace.droolstemplete.service;

import com.wjpspace.droolstemplete.dao.CustomerDao;
import com.wjpspace.droolstemplete.entity.Customer;
import com.wjpspace.droolstemplete.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieSession;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author wjp
 * @date 2020/1/20 16:09
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerDao customerDao;

    private static final String SERVER_URL = "http://10.139.60.81:8280/kie-server/services/rest/server";
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";
    private static final String KIE_CONTAINER_ID = "droolsTemplete";
    /**
     * 规则文件1
     */
    private static final String KIE_SESSION_ID = "ksession-rules";
    /**
     * 规则文件2
     */
    private static final String KIE_SESSION_ID2 = "ksession-rules2";

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
            //转换成数据实体
            Person person = new Person();
            BeanUtils.copyProperties(customer, person);
            person.setStep(customer.getStatus());

            //执行规则并拿到结果
            kieSession.insert(person);
            kieSession.fireAllRules();

            //符合规则1的加入到集合，准备进行第二遍规则匹配
            if (person.getValid()) {
                Customer c = customerList.stream().filter(customer1 -> customer1.getId().equals(person.getId())).collect(Collectors.toList()).get(0);
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

    @Override
    public void go2() {
        RuleServicesClient rules = getRuleServicesClient();
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();

        ArrayList<Customer> customers = new ArrayList<>();

        //获取原始数据
        List<Customer> customerList = customerDao.getAllCustomers();
        //遍历数据并匹配第一个规则文件
        for (Customer customer : customerList) {
            List<Command<?>> commands = new LinkedList<Command<?>>();

            Person person = new Person();
            BeanUtils.copyProperties(customer, person);
            person.setStep(customer.getStatus());

            //执行规则并获取执行结果
            Person person2 = getPerson(rules, cmdFactory, person, commands, KIE_SESSION_ID);

            //满足规则的加入奥集合中准备第二个规则文件的匹配
            if (person2.getValid()) {
                Customer c = customerList.stream().filter(customer1 ->
                        person2.getId().equals(customer1.getId())
                ).collect(Collectors.toList()).get(0);
                customers.add(c);
            }
        }
        //匹配第二个规则文件
        sendMessage2(customers);

    }

    private void sendMessage2(ArrayList<Customer> customers) {
        //随机改变数据对象
        List<Person> personList = changeCustomerStep(customers);

        ArrayList<Long> idList = new ArrayList<>();

        if (CollectionUtils.isEmpty(personList)) {
            System.out.println("当前无数据可做规则校验修改");
            throw new RuntimeException("当前无数据可做规则校验修改");
        }


        RuleServicesClient rules = getRuleServicesClient();
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();
        for (Person person : personList) {
            List<Command<?>> commands = new LinkedList<Command<?>>();

            //执行第二个规则文件并获取结果
            Person person2 = getPerson(rules, cmdFactory, person, commands, KIE_SESSION_ID2);
            //满足规则文件的加入到ID集合中
            if (person2.getValid()) {
                idList.add(person.getId());
            }
        }
        System.out.println("需要被推送到电销系统的是：");
        System.out.println(idList);
    }

    private Person getPerson(RuleServicesClient rules, KieCommands cmdFactory, Person person, List<Command<?>> commands, String kieSessionId2) {
        commands.add(cmdFactory.newInsert(person, "person"));
        commands.add(cmdFactory.newFireAllRules());
        ServiceResponse<ExecutionResults> response = rules.executeCommandsWithResults(KIE_CONTAINER_ID,
                cmdFactory.newBatchExecution(commands, kieSessionId2));

        ExecutionResults result = response.getResult();
        return (Person) result.getValue("person");
    }


    private RuleServicesClient getRuleServicesClient() {
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(SERVER_URL, USERNAME, PASSWORD);
        config.setMarshallingFormat(MarshallingFormat.JSON);
        config.setTimeout(30000L);

        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        return client.getServicesClient(RuleServicesClient.class);
    }

    private List<Person> changeCustomerStep(List<Customer> customers) {

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
