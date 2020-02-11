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
    private static final String KIE_SESSION_ID = "ksession-rules";
    private static final String KIE_SESSION_ID2 = "ksession-rules2";


    @Override
    public void addUser() {
        Customer customer = new Customer();
        customer.setAge(10);
        customerDao.save(customer);
    }


    @Override
    public void go() {
        ArrayList<Customer> customers = new ArrayList<>();

        List<Customer> customerList = customerDao.getAllCustomers();

        KieServices kieServices = KieServices.Factory.get();
        KieSession kieSession = kieServices.getKieClasspathContainer().newKieSession("ksession-rules");

        for (Customer customer : customerList) {
            Person person = new Person();
            BeanUtils.copyProperties(customer, person);
            person.setStep(customer.getStatus());
            kieSession.insert(person);
            kieSession.fireAllRules();
            if (person.getValid()) {
                Customer c = customerList.stream().filter(customer1 -> customer1.getId().equals(person.getId())).collect(Collectors.toList()).get(0);
                customers.add(c);
            }
        }
        sendMessage(customers);
    }

    @Override
    public void go2() {
        RuleServicesClient rules = getRuleServicesClient();
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();

        ArrayList<Customer> customers = new ArrayList<>();

        List<Customer> customerList = customerDao.getAllCustomers();
        for (Customer customer : customerList) {
            List<Command<?>> commands = new LinkedList<Command<?>>();

            Person person = new Person();
            BeanUtils.copyProperties(customer, person);
            person.setStep(customer.getStatus());

            commands.add(cmdFactory.newInsert(person, "person"));
            commands.add(cmdFactory.newFireAllRules());
            ServiceResponse<ExecutionResults> response = rules.executeCommandsWithResults(KIE_CONTAINER_ID,
                    cmdFactory.newBatchExecution(commands, KIE_SESSION_ID));

            ExecutionResults result = response.getResult();
            Person person2 = (Person) result.getValue("person");
            if (person2.getValid()) {
                Customer c = customerList.stream().filter(customer1 ->
                        person2.getId().equals(customer1.getId())
                ).collect(Collectors.toList()).get(0);
                customers.add(c);
            }
        }
        sendMessage2(customers);

    }

    private void sendMessage2(ArrayList<Customer> customers) {
        List<Person> personList = changeCustomerStep(customers);
        if (CollectionUtils.isEmpty(personList)) {
            System.out.println("当前无数据可做规则校验修改");
        }
        ArrayList<Long> idList = new ArrayList<>();

        RuleServicesClient rules = getRuleServicesClient();
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();
        for (Person person : personList) {
            List<Command<?>> commands = new LinkedList<Command<?>>();

            commands.add(cmdFactory.newInsert(person, "person"));
            commands.add(cmdFactory.newFireAllRules());
            ServiceResponse<ExecutionResults> response = rules.executeCommandsWithResults(KIE_CONTAINER_ID,
                    cmdFactory.newBatchExecution(commands, KIE_SESSION_ID2));

            ExecutionResults result = response.getResult();
            Person person2 = (Person) result.getValue("person");
            if (person2.getValid()) {
                idList.add(person.getId());
            }
        }
        System.out.println("需要被推送到电销系统的是：");
        System.out.println(idList);
    }


    private void sendMessage(List<Customer> customers) {

        List<Person> personList = changeCustomerStep(customers);
        if (CollectionUtils.isEmpty(personList)) {
            System.out.println("当前无数据可做规则校验修改");
        }
        ArrayList<Long> idList = new ArrayList<>();

        KieServices kieServices = KieServices.Factory.get();
        KieSession kieSession = kieServices.getKieClasspathContainer().newKieSession("ksession-rules2");

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
