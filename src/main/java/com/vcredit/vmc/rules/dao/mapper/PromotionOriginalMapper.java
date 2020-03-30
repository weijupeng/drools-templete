package com.vcredit.vmc.rules.dao.mapper;

import com.vcredit.vmc.rules.dao.entity.PromotionOriginal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wjp
 * @date 2020/3/2 14:03
 */
@Mapper
public interface PromotionOriginalMapper extends SuperMapper<PromotionOriginal> {
    /**
     * 查找老标签
     * @param productCode 产品代码
     * @param mobile      手机号
     * @return string
     */
    String selectLastLabel(@Param("productCode") String productCode, @Param("mobile") String mobile);
}
