package com.wjpspace.droolstemplete.service;

import com.vcredit.framework.fmp.kie.server.client.service.DroolsRuleEngine;
import com.vcredit.framework.fmp.kie.server.client.service.RuleParams;
import com.vcredit.framework.fmp.kie.server.client.service.RuleParamsBuilder;
import com.vcredit.framework.fmp.kie.server.client.service.RuleResults;
import com.vcredit.vmc.rules.drools.input.AntiCheckInput;
import com.vcredit.vmc.rules.drools.output.AntiCheckOutput;
import com.vcredit.vmc.rules.fact.TaskNoGetLabelAndPlanFact;
import com.wjpspace.droolstemplete.dao.PromotionOriginalDao;
import com.wjpspace.droolstemplete.entity.*;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wjp
 * @date 2020/3/16 14:52
 */
@Service
public class PromotionServiceImpl implements PromotionService {
    @Resource
    private PromotionOriginalDao promotionOriginalDao;
    @Resource
    private DroolsRuleEngine droolsRuleEngine;

    private static Set<String> specialChannelFilterDataSource = initSpecialChannelFilterDataSource();

    @Override
    public void stream(PromotionTaskRuleInput input) {
        String label = selectBeforeLabel(input);
        TaskNoGetLabelAndPlanFact fact = new TaskNoGetLabelAndPlanFact();
        BeanUtils.copyProperties(input, fact);
        fact.setOldTaskLabel(label);
        //获取规则1的kieSession
        KieServices kieServices = KieServices.Factory.get();
        KieSession kieSession = kieServices.getKieClasspathContainer().newKieSession("defaultRule");

        //执行规则并拿到结果
        kieSession.insert(fact);
        kieSession.fireAllRules();
        kieSession.dispose();

        System.out.println(fact);
    }

    @Override
    public void rule(PromotionTaskRuleInput input) {

    }

    private String selectBeforeLabel(PromotionTaskRuleInput input) {
        //获取历史记录
        List<PromotionOriginal> getBeforeOriginal = promotionOriginalDao.selectLastLabel(input);
        if (CollectionUtils.isEmpty(getBeforeOriginal)) {
            return null;
        }
        //获取最新一条
        Optional<PromotionOriginal> first = getBeforeOriginal.stream().max(Comparator.comparing(PromotionOriginal::getUpdatedTime));
        return first.map(PromotionOriginal::getPromoteTasksLabel).orElse(null);
    }

    /**
     * 初始化特殊渠道
     * @return 特殊渠道
     */
    private static Set<String> initSpecialChannelFilterDataSource() {
        if (Objects.isNull(specialChannelFilterDataSource)) {
            specialChannelFilterDataSource = new HashSet<>();
            specialChannelFilterDataSource.add("zhongxinyinhangh501");
        }
        return specialChannelFilterDataSource;
    }

    @Override
    public TelemarketingPromotionRuleOutput rule2(TelemarketingPromotionRuleInput input) {
        TelemarketingPromotionRuleOutput output = new TelemarketingPromotionRuleOutput();
        AntiCheckInput fact = initFact(input);
        AntiCheckOutput ruleOutput = new AntiCheckOutput();


        KieServices kieServices = KieServices.Factory.get();
        KieSession kieSession = kieServices.getKieClasspathContainer().newKieSession("antiCheckRule");
        //执行规则并拿到结果
        kieSession.insert(fact);
        kieSession.setGlobal("specialChannelFilterDataSource",specialChannelFilterDataSource);
        kieSession.insert(ruleOutput);
        kieSession.fireAllRules();
        kieSession.dispose();
        if (Objects.nonNull(ruleOutput.getResult())) {
            BeanUtils.copyProperties(ruleOutput, output);
            return output;
        }
        output.setResult(Boolean.TRUE);
        return output;
    }

    private AntiCheckInput initFact(TelemarketingPromotionRuleInput input) {
        int stepOrdinal = BusinessStepEnum.valueOf(input.getStep()).ordinal();
        int currentOrdinal = BusinessStepEnum.valueOf(input.getCurrentStep()).ordinal();
        AntiCheckInput fact = new AntiCheckInput();

        BeanUtils.copyProperties(input, fact);
        fact.setCurrentStepNumber(currentOrdinal);
        fact.setStepNumber(stepOrdinal);
        return fact;
    }


    @Override
    public TelemarketingPromotionRuleOutput rule3(TelemarketingPromotionRuleInput input) {
        TelemarketingPromotionRuleOutput output = new TelemarketingPromotionRuleOutput();
        AntiCheckInput fact = initFact(input);
        AntiCheckOutput ruleOutput = new AntiCheckOutput();


        output.setResult(Boolean.TRUE);
        RuleParams ruleParam = RuleParamsBuilder.create(input.getKieContainerId(), input.getKieSessionId())
                .setGlobal("specialChannelFilterDataSource", initSpecialChannelFilterDataSource())
                .insert("input", fact)
                .insert("output", ruleOutput)
                .build();

        RuleResults ruleResult = droolsRuleEngine.call(ruleParam);

        if (ruleResult.isSuccess()) {
            AntiCheckOutput output1 = ruleResult.getValue("output");
            if (Objects.isNull(output1.getResult())) {
                output.setResult(Boolean.TRUE);
            } else {
                BeanUtils.copyProperties(output1, output);
            }
            return output;
        }
        return output;
    }
}
