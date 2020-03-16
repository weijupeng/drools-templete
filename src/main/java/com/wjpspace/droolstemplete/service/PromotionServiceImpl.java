package com.wjpspace.droolstemplete.service;

import com.wjpspace.droolstemplete.dao.PromotionOriginalDao;
import com.wjpspace.droolstemplete.entity.PromotionOriginal;
import com.wjpspace.droolstemplete.entity.PromotionTaskRuleInput;
import com.vcredit.vmc.rules.fact.TaskNoGetLabelAndPlanFact;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author wjp
 * @date 2020/3/16 14:52
 */
@Service
public class PromotionServiceImpl implements PromotionService {
    @Resource
    private PromotionOriginalDao promotionOriginalDao;
    @Override
    public void stream(PromotionTaskRuleInput input) {
        String label = selectBeforeLabel(input);
        TaskNoGetLabelAndPlanFact fact = new TaskNoGetLabelAndPlanFact();
        BeanUtils.copyProperties(input,fact);
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


    private String selectBeforeLabel(PromotionTaskRuleInput input) {
        //获取历史记录
        List<PromotionOriginal> getBeforeOriginal = promotionOriginalDao.selectLastLabel(input);
        //队列减去刚插入的一条
//        getBeforeOriginal.remove(getBeforeOriginal.size() - 1);
        if (CollectionUtils.isEmpty(getBeforeOriginal)) {
            return null;
        }
        //获取最新一条
        Optional<PromotionOriginal> first = getBeforeOriginal.stream().max(Comparator.comparing(PromotionOriginal::getUpdatedTime));
        return first.map(PromotionOriginal::getPromoteTasksLabel).orElse(null);
    }
}
