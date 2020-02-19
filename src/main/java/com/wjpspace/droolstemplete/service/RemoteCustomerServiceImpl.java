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
import org.springframework.util.CollectionUtils;

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
        //获取远程连接
        RuleServicesClient client = getRuleServicesClient();
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();

        //获取原始数据
        List<Customer> customerList = customerDao.getAllCustomers();

        //创建接收
        ArrayList<Customer> customers = new ArrayList<>();

        //遍历数据并匹配第一个规则文件
        for (Customer customer : customerList) {

            Person person = new Person();
            BeanUtils.copyProperties(customer, person);
            person.setStep(customer.getStatus());

            //执行规则并获取执行结果
            Person person2 = getPerson(client, cmdFactory, person, KIE_SESSION_ID);

            //满足规则的加入到集合中准备第二个规则文件的匹配
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
        //随机改变数据对象，给对象加上当前状态
        List<Person> personList = changeCustomerStep(customers);

        //创建接收
        ArrayList<Long> idList = new ArrayList<>();

        if (CollectionUtils.isEmpty(personList)) {
            System.out.println("当前无数据可做规则校验修改");
            throw new RuntimeException("当前无数据可做规则校验修改");
        }

        //获取远程连接
        RuleServicesClient client = getRuleServicesClient();
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();


        //遍历数据并匹配第二个规则文件
        for (Person person : personList) {


            //执行第二个规则文件并获取结果
            Person person2 = getPerson(client, cmdFactory, person, KIE_SESSION_ID2);
            //满足规则文件的加入到ID集合中
            if (person2.getValid()) {
                idList.add(person.getId());
            }
        }
        System.out.println("需要被推送到电销系统的是：");
        System.out.println(idList);
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
     * 获取规则执行完的结果
     * @param client
     * @param cmdFactory
     * @param person
     * @param kieSessionId
     * @return
     */
    private Person getPerson(RuleServicesClient client, KieCommands cmdFactory, Person person, String kieSessionId) {
        List<Command<?>> commands = new LinkedList<>();
        commands.add(cmdFactory.newInsert(person, "person"));
        commands.add(cmdFactory.newFireAllRules());
        ServiceResponse<ExecutionResults> response = client.executeCommandsWithResults(KIE_CONTAINER_ID,
                cmdFactory.newBatchExecution(commands, kieSessionId));

        ExecutionResults result = response.getResult();
        return (Person) result.getValue("person");
    }
}
