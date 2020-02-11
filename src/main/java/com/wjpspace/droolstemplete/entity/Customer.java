package com.wjpspace.droolstemplete.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjp
 * @date 2020/1/20 15:07
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("t_customer")
public class Customer {
    @TableId
    private Long id;
    private String name;
    private Integer age;
    private Integer status;

}
