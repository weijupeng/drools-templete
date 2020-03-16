package com.wjpspace.droolstemplete.task;

import com.wjpspace.droolstemplete.entity.Person;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wjp
 * @date 2020/1/21 16:15
 */
@Configuration
@EnableScheduling
@Slf4j
public class DroolsTask {

    private static final String SERVER_URL = "http://10.139.60.81:8280/kie-server/services/rest/server";
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";
    private static final String KIE_CONTAINER_ID = "droolsTemplete";
    private static final String KIE_SESSION_ID = "rules-session";


    public static void main(String[] args) {

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(SERVER_URL, USERNAME, PASSWORD);
        config.setMarshallingFormat(MarshallingFormat.JSON);
        config.setTimeout(30000L);

        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        RuleServicesClient rules = client.getServicesClient(RuleServicesClient.class);
        KieCommands cmdFactory = KieServices.Factory.get().getCommands();
        List<Command<?>> commands = new LinkedList<Command<?>>();

        Person person = new Person();
        person.setAge(12);
        person.setName("123");
        person.setValid(true);
        commands.add(cmdFactory.newInsert(person, "person"));
        commands.add(cmdFactory.newFireAllRules());
        ServiceResponse<ExecutionResults> response = rules.executeCommandsWithResults(KIE_CONTAINER_ID,
                cmdFactory.newBatchExecution(commands, KIE_SESSION_ID));

        ExecutionResults result = response.getResult();
        person = (Person) result.getValue("person");
        System.out.println(person.getAge());
        System.out.println(person.getName());
        System.out.println(person.getValid());
    }
}
