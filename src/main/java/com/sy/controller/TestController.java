package com.sy.controller;

import com.sy.base.BaseController;
import com.sy.entity.Person;
import com.sy.mybatis.domain.Page;
import com.sy.mybatis.domain.SimplePage;
import com.sy.service.TestService;
import com.sy.util.Result;
import com.sy.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 11:33
 * @Descript:
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController<Person> {

    @Autowired
    private TestService testService;


}
