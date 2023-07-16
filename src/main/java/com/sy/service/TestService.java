package com.sy.service;

import com.sy.base.BaseService;
import com.sy.entity.Person;
import com.sy.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 20:43
 * @Descript:
 */
@Service
public class TestService extends BaseService<Person> {

    @Resource
    private TestMapper mapper;

    public List<Person> test() {
        return mapper.test();
    }


}
