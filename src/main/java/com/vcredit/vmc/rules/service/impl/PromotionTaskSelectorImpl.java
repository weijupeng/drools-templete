package com.vcredit.vmc.rules.service.impl;

import com.vcredit.vmc.rules.common.DTO.PromotionInputData;
import com.vcredit.vmc.rules.dao.dao.PromotionTaskDao;
import com.vcredit.vmc.rules.dao.entity.PromotionTask;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * PromotionTaskSelectorDbImpl
 * 默认：数据库方式实现
 * <p>
 * @author: kancy
 * @date: 2020/3/2 14:25
 **/
@Component
public class PromotionTaskSelectorImpl {
    @Resource
    private PromotionTaskDao promotionTaskDao;

    /**
     * 找到一个营销任务
     * @param inputData 入参
     * @return PromotionTaskData
     */
    public PromotionTask findPromotionTask(PromotionInputData inputData) {
        //通过入参找任务
        List<PromotionTask> currentProdTask = promotionTaskDao.getCurrentProdTask(inputData);
        Optional<PromotionTask> taskOptional = getPromotionTask(currentProdTask);

        //不为空 返回任务
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }

        //上一步为空，模糊产品
        List<PromotionTask> allProdTask = promotionTaskDao.getAllProdTask(inputData);
        Optional<PromotionTask> taskOptional2 = getPromotionTask(allProdTask);

        if (taskOptional2.isPresent()) {
            return taskOptional2.get();
        }

        throw new RuntimeException(String.format("找不到该条数据[%s]的营销任务", inputData));
    }


    private Optional<PromotionTask> getPromotionTask(List<PromotionTask> tasks) {
        if (!CollectionUtils.isEmpty(tasks)) {
            List<PromotionTask> collect = tasks.stream()
                    .sorted((o1, o2) -> o2.getTaskSort() - o1.getTaskSort()).collect(Collectors.toList());
            return Optional.of(collect.get(0));
        }
        return Optional.empty();
    }
}
