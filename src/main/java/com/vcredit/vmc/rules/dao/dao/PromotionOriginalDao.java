package com.vcredit.vmc.rules.dao.dao;

import com.vcredit.vmc.rules.dao.entity.PromotionOriginal;
import com.vcredit.vmc.rules.dao.mapper.PromotionOriginalMapper;
import org.springframework.stereotype.Repository;


/**
 * @author wjp
 */
@Repository
public class PromotionOriginalDao extends BaseDAO<PromotionOriginalMapper, PromotionOriginal> {

    /**
     * 根据手机号和产品从远离记录表查数据
     * @return List<PromotionOriginal>
     */
    public String selectLastLabel(String productCode, String mobile) {
        return baseMapper.selectLastLabel(productCode, mobile);
    }
}