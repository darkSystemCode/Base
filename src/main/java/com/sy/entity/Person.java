package com.sy.entity;

import com.sy.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 19:55
 * @Descript:
 */
@Data
public class Person extends BaseEntity {
    private String name;
    private Integer age;
    private Date birthday;
}
