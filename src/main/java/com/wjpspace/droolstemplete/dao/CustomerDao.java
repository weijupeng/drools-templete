package com.wjpspace.droolstemplete.dao;

import com.wjpspace.droolstemplete.dao.mapper.CustomerMapper;
import com.wjpspace.droolstemplete.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wjp
 * @date 2020/1/20 16:03
 */
@Repository
public class CustomerDao extends SuperDao<CustomerMapper, Customer> {

    public List<Customer> getCustomerList(Integer value) {
        return find(Customer.builder().status(value).build());
    }

    public List<Customer> getAllCustomers() {
        return find(Customer.builder().build());
    }
}
