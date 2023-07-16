package com.sy.mapper;

import com.sy.base.BaseMapper;
import com.sy.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 20:17
 * @Descript:
 */
@Repository
public interface TestMapper extends BaseMapper<Person> {

    List<Person> test();

}
