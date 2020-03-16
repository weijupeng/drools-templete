package com.wjpspace.droolstemplete.service;

import com.wjpspace.droolstemplete.dao.CustomerDao;
import com.wjpspace.droolstemplete.entity.Customer;
import com.wjpspace.droolstemplete.entity.Person;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wjp
 * @date 2020/2/18 14:48
 */
@Service
public class RemoteCustomerServiceImpl extends AbstractService implements RemoteCustomerService {

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

    @Resource
    private CustomerDao customerDao;

    @Override
    public void go2() {

        //获取原始数据
//        List<Customer> customerList = customerDao.getAllCustomers();

        ArrayList<Customer> customerList = new ArrayList<>();

        customerList.add(new Customer(1L, "name1", 20, 1));
        customerList.add(new Customer(2L, "name2", 20, 1));
        customerList.add(new Customer(3L, "name3", 20, 1));
        customerList.add(new Customer(4L, "name4", 20, 1));

        //转换实体数据
        List<Person> personList = getFacts(customerList);

        //执行第一个规则文件并获取结果
        ArrayList<Person> customers = getRule1Result(personList);

        //执行第二个规则文件并拿到需要推送用户的ID
        ArrayList<Long> idList = getRule2Result(customers);

        System.out.println("需要被推送到电销系统的是：");
        System.out.println("" + idList);
    }

    private ArrayList<Long> getRule2Result(ArrayList<Person> customers) {
        List<Person> personList2 = changeCustomerStep(customers);

        //执行规则并获取结果
        ExecutionResults result = getExecutionResults(personList2, KIE_SESSION_ID2);

        //创建接收
        ArrayList<Long> idList = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++) {
            Person value = (Person) result.getValue("person" + i);
            if (value.getValid()) {
                idList.add(value.getId());
            }
        }
        return idList;
    }

    private ArrayList<Person> getRule1Result(List<Person> personList) {
        ExecutionResults result = getExecutionResults(personList, KIE_SESSION_ID);

        //创建接收
        ArrayList<Person> customers = new ArrayList<>();

        for (int i = 0; i < personList.size(); i++) {
            Person value = (Person) result.getValue("person" + i);
            if (value.getValid()) {
                customers.add(value);
            }
        }
        return customers;
    }

    private List<Person> getFacts(List<Customer> customerList) {
        return customerList.stream().map(customer -> {
            Person person = new Person();
            BeanUtils.copyProperties(customer, person);
            person.setStep(customer.getStatus());
            return person;
        }).collect(Collectors.toList());
    }

    /**
     * 获取远程连接
     * @return RuleServicesClient
     */
    private RuleServicesClient getRuleServicesClient() {
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(SERVER_URL, USERNAME, PASSWORD);
        config.setMarshallingFormat(MarshallingFormat.JSON);
        config.setTimeout(30000L);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        return client.getServicesClient(RuleServicesClient.class);
    }

    /**
     * 批量进行规则运算
     * @param personList   用户列表
     * @param kieSessionId kieSessionId
     * @return ExecutionResults
     */
    private ExecutionResults getExecutionResults(List<Person> personList, String kieSessionId) {
        //获取远程连接
        RuleServicesClient client = getRuleServicesClient();
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();

        List<Command<?>> commands = new LinkedList<>();
        for (int i = 0; i < personList.size(); i++) {
            commands.add(cmdFactory.newInsert(personList.get(i), "person" + i));
        }
        commands.add(cmdFactory.newFireAllRules());
        ServiceResponse<ExecutionResults> response = client.executeCommandsWithResults(KIE_CONTAINER_ID,
                cmdFactory.newBatchExecution(commands, kieSessionId));

        return response.getResult();
    }


}
