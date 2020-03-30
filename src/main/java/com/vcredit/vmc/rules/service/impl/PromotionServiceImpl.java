package com.vcredit.vmc.rules.service.impl;

import com.vcredit.framework.fmp.kie.server.client.service.DroolsRuleEngine;
import com.vcredit.framework.fmp.kie.server.client.service.RuleParams;
import com.vcredit.framework.fmp.kie.server.client.service.RuleParamsBuilder;
import com.vcredit.framework.fmp.kie.server.client.service.RuleResults;
import com.vcredit.vmc.rules.common.DTO.PromotionInputData;
import com.vcredit.vmc.rules.common.DTO.SmsPromotionRuleInput;
import com.vcredit.vmc.rules.common.DTO.TelemarketingPromotionRuleInput;
import com.vcredit.vmc.rules.common.enums.BusinessStepEnum;
import com.vcredit.vmc.rules.dao.dao.PromotionOriginalDao;
import com.vcredit.vmc.rules.dao.entity.PromotionTask;
import com.vcredit.vmc.rules.drools.input.PromotionLabelAndPlanInput;
import com.vcredit.vmc.rules.drools.input.SmsAntiCheckInput;
import com.vcredit.vmc.rules.drools.input.TeleAntiCheckInput;
import com.vcredit.vmc.rules.drools.output.PromotionLabelAndPlanOutput;
import com.vcredit.vmc.rules.drools.output.SmsAntiCheckOutput;
import com.vcredit.vmc.rules.drools.output.TeleAntiCheckOutput;
import com.vcredit.vmc.rules.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wjp
 * @date 2020/3/27 18:53
 */
@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {
    @Resource
    private PromotionOriginalDao promotionOriginalDao;
    @Resource
    private DroolsRuleEngine droolsRuleEngine;
    @Resource
    private PromotionTaskSelectorImpl promotionTaskSelector;

    @Override
    public PromotionLabelAndPlanOutput check1(PromotionInputData input) {
        PromotionTask task = promotionTaskSelector.findPromotionTask(input);
        String oldLabel = promotionOriginalDao.selectLastLabel(input.getProductCode(), input.getMobile());
        PromotionLabelAndPlanInput fact = new PromotionLabelAndPlanInput();
        fact.setOldTaskLabel(oldLabel);
        fact.setEventType(input.getStep());
        fact.setTaskNo(task.getTaskNo());
        RuleParams ruleParam = RuleParamsBuilder.create(input.getKieContainerId(), input.getKieSessionId())
                .insert("fact", fact)
                .insert("output", new PromotionLabelAndPlanOutput())
                .build();
        RuleResults ruleResult = droolsRuleEngine.call(ruleParam);
        log.info("ruleResult : {}", ruleResult);

        if (ruleResult.isSuccess()) {
            PromotionLabelAndPlanOutput output = ruleResult.getValue("output");
            return output;
        }
        throw new RuntimeException(String.format("找不到该条数据[%s]的规则处理方案", input));
    }


    @Override
    public SmsAntiCheckOutput check2(SmsPromotionRuleInput input) {
        int stepOrdinal = BusinessStepEnum.valueOf(input.getStep()).ordinal();
        int currentOrdinal = BusinessStepEnum.valueOf(input.getCurrentStep()).ordinal();
        SmsAntiCheckInput fact = new SmsAntiCheckInput();

        BeanUtils.copyProperties(input, fact);
        fact.setCurrentStepNumber(currentOrdinal);
        fact.setStepNumber(stepOrdinal);

        RuleParams ruleParam = RuleParamsBuilder.create(input.getKieContainerId(), input.getKieSessionId())
                .setGlobal("specialChannelFilterDataSource", initSpecialChannelFilterDataSource())
                .insert("input", fact)
                .insert("output", new SmsAntiCheckOutput())
                .build();
        //执行并获取结果
        RuleResults ruleResult = droolsRuleEngine.call(ruleParam);

        //结果判断
        if (ruleResult.isSuccess()) {
            SmsAntiCheckOutput checkOutput = ruleResult.getValue("output");
            return checkOutput;
        }
        throw new RuntimeException(String.format("该条数据[%s]的规则处理失败", input));
    }


    @Override
    public TeleAntiCheckOutput check3(TelemarketingPromotionRuleInput input) {
        int stepOrdinal = BusinessStepEnum.valueOf(input.getStep()).ordinal();
        int currentOrdinal = BusinessStepEnum.valueOf(input.getCurrentStep()).ordinal();
        TeleAntiCheckInput fact = new TeleAntiCheckInput();

        BeanUtils.copyProperties(input, fact);
        fact.setCurrentStepNumber(currentOrdinal);
        fact.setStepNumber(stepOrdinal);
        //封装参数
        RuleParams ruleParam = RuleParamsBuilder.create(input.getKieContainerId(), input.getKieSessionId())
                .setGlobal("specialChannelFilterDataSource", initSpecialChannelFilterDataSource())
                .insert("input", fact)
                .insert("output", new TeleAntiCheckOutput())
                .build();

        // 执行并获取结果
        RuleResults ruleResult = droolsRuleEngine.call(ruleParam);

        //结果判断
        if (ruleResult.isSuccess()) {
            TeleAntiCheckOutput checkOutput = ruleResult.getValue("output");
            return checkOutput;
        }
        throw new RuntimeException(String.format("该条数据[%s]的规则处理失败", input));
    }

    /**
     * 初始化特殊渠道
     * @return 特殊渠道
     */
    private static Set<String> initSpecialChannelFilterDataSource() {
        HashSet<String> specialChannelFilterDataSource = new HashSet<>();
        specialChannelFilterDataSource.add("zhongxinyinhangh501");
        return specialChannelFilterDataSource;
    }
}
