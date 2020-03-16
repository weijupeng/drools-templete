package com.wjpspace.droolstemplete.dao;

import com.wjpspace.droolstemplete.dao.mapper.PromotionOriginalMapper;
import com.wjpspace.droolstemplete.entity.PromotionOriginal;
import com.wjpspace.droolstemplete.entity.PromotionTaskRuleInput;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wjp
 */
@Repository
public class PromotionOriginalDao extends BaseDAO<PromotionOriginalMapper, PromotionOriginal> {
    /**
     * @param po
     */
    public void insert(PromotionOriginal po) {
        baseMapper.insert(po);
    }

    /**
     * 根据手机号和产品从远离记录表查数据
     * @param input 入参
     * @return List<PromotionOriginal>
     */
    public List<PromotionOriginal> selectLastLabel(PromotionTaskRuleInput input) {
        PromotionOriginal promotionOriginal = new PromotionOriginal();
        promotionOriginal.setMobile(input.getMobile());
        promotionOriginal.setProductCode(input.getProductCode());
        return list(promotionOriginal);
    }

    /**
     * 根据主键查询
     * @param originalId
     * @return
     */
    public PromotionOriginal selectById(Long originalId) {
        return baseMapper.selectById(originalId);
    }
}