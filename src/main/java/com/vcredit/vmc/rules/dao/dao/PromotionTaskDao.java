package com.vcredit.vmc.rules.dao.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vcredit.vmc.rules.common.DTO.PromotionInputData;
import com.vcredit.vmc.rules.common.enums.YesOrNoEnum;
import com.vcredit.vmc.rules.common.utils.DateUtils;
import com.vcredit.vmc.rules.dao.entity.PromotionTask;
import com.vcredit.vmc.rules.dao.mapper.PromotionTaskMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author wjp
 * @date 2020/3/2 14:16
 */
@Repository
public class PromotionTaskDao extends BaseDAO<PromotionTaskMapper, PromotionTask> {

    private static final String ALL_PRODUCT = "all";

    /**
     * 精准匹配 通过产品和来源
     * @param inputData 入参
     * @return Optional<PromotionTask>
     */
    public List<PromotionTask> getCurrentProdTask(PromotionInputData inputData) {
        LambdaQueryWrapper<PromotionTask> lambdaQuery = getWrapper(inputData);
        lambdaQuery.eq(PromotionTask::getProductCode, inputData.getProductCode());
        return list(lambdaQuery);
    }

    /**
     * 模糊产品 所有产品和来源
     * @param inputData 入参
     * @return Optional<PromotionTask>
     */
    public List<PromotionTask> getAllProdTask(PromotionInputData inputData) {
        LambdaQueryWrapper<PromotionTask> wrapper = getWrapper(inputData);
        wrapper.eq(PromotionTask::getProductCode, ALL_PRODUCT);
        return list(wrapper);
    }

    private LambdaQueryWrapper<PromotionTask> getWrapper(PromotionInputData inputData) {
        LambdaQueryWrapper<PromotionTask> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(PromotionTask::getIsValid, YesOrNoEnum.YES.getCode());
        lambdaQuery.eq(PromotionTask::getPromoteSource, inputData.getPromoteSource());
        String date = DateUtils.dateToString(new Date(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
        lambdaQuery.lt(PromotionTask::getStartTime, date);
        lambdaQuery.gt(PromotionTask::getEndTime, date);

        return lambdaQuery;
    }


}
