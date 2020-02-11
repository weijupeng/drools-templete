package com.wjpspace.droolstemplete.dao.mapper;

import com.wjpspace.droolstemplete.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wjp
 * @date 2020/1/20 16:03
 */
@Mapper
public interface CustomerMapper extends SupperMapper<Customer> {

    List<Customer> selectLists(@Param("step") Integer status);
}
