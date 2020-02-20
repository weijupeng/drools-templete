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
        List<Customer> customerList = customerDao.getAllCustomers();

        //转换实体数据
        List<Person> personList = customerList.stream().map(customer -> {
            Person person = new Person();
            BeanUtils.copyProperties(customer, person);
            person.setStep(customer.getStatus());
            return person;
        }).collect(Collectors.toList());

        ExecutionResults result = getExecutionResults(personList, KIE_SESSION_ID);

        //创建接收
        ArrayList<Person> customers = new ArrayList<>();

        for (int i = 0; i < personList.size(); i++) {
            Person value = (Person) result.getValue("person" + i);
            if (value.getValid()) {
                customers.add(value);
            }
        }

        //匹配第二个规则文件
        checkRules2(customers);
    }


    /**
     * 执行规则文件2
     * @param people 通过文件1的用户
     */
    private void checkRules2(ArrayList<Person> people) {
        //随机改变数据对象，给对象加上当前状态
        List<Person> personList = changeCustomerStep(people);

        //执行规则并获取结果
        ExecutionResults result = getExecutionResults(personList, KIE_SESSION_ID2);

        //创建接收
        ArrayList<Long> idList = new ArrayList<>();
        for (int i = 0; i < personList.size(); i++) {
            Person value = (Person) result.getValue("person" + i);
            if (value.getValid()) {
                idList.add(value.getId());
            }
        }

        System.out.println("需要被推送到电销系统的是：");
        System.out.println(idList);
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


}
