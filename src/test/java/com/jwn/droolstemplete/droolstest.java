package com.jwn.droolstemplete;


import com.wjpspace.droolstemplete.common.enums.BusinessStepEnum;
import com.wjpspace.droolstemplete.entity.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = droolstest.class)
@RunWith(SpringRunner.class)
public class droolstest {
//    @Resource
//    private CustomerDao customerDao;


    private static final String SERVER_URL = "http://10.139.60.81:8280/kie-server/services/rest/server";
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";
    private static final String KIE_CONTAINER_ID = "droolsTemplete";
    private static final String KIE_SESSION_ID = "myAgeSession";

    private KieServicesConfiguration config = null;

    @Before
    public void before() {
        config = KieServicesFactory.newRestConfiguration(SERVER_URL, USERNAME, PASSWORD);
        config.setMarshallingFormat(MarshallingFormat.JSON);
        config.setTimeout(30000L);
    }

    @Test
    public void test2() {
        KieServices kieServices = KieServices.Factory.get();
        KieSession kieSession = kieServices.getKieClasspathContainer().newKieSession("ksession-rules");

        Person person = new Person();
        person.setStep(BusinessStepEnum.CREDIT_2.getValue());
        kieSession.insert(person);
        kieSession.insert(person);
        kieSession.fireAllRules();
        System.out.println(person);
        if (person.getValid()) {
            System.out.println("需要推电销");
        } else {
            System.out.println("不需要推送");
        }
    }


//    @Test
//    public void test1() {
//
//        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
//        RuleServicesClient rules = client.getServicesClient(RuleServicesClient.class);
//        KieCommands cmdFactory = KieServices.Factory.get().getCommands();
//        List<Command<?>> commands = new LinkedList<Command<?>>();
//
//        Person person = new Person();
//        person.setAge(12);
//        person.setName("123");
//        person.setValid(true);
//        commands.add(cmdFactory.newInsert(person, "person"));
//        commands.add(cmdFactory.newFireAllRules());
//        ServiceResponse<org.kie.api.runtime.ExecutionResults> response = rules.executeCommandsWithResults(KIE_CONTAINER_ID,
//                cmdFactory.newBatchExecution(commands, KIE_SESSION_ID));
//
//        ExecutionResults result = response.getResult();
//        person = (Person) result.getValue("person");
//        System.out.println(person.getAge());
//        System.out.println(person.getName());
//        System.out.println(person.isValid());
//
//    }
}