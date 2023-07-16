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

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 11:33
 * @Descript: 每个Controller必须在类名上使用@RequestMapping()注解指定其顶层访问路径，避免同名controller注入spring bean中，导致异常错误
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController<Person> {

    @Resource
    private TestService testService;

    @GetMapping("/getList")
    public Result test() {
        return ResultUtil.success(testService.test());
    }


}
