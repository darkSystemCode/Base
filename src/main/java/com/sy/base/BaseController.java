package com.sy.base;

import com.sy.entity.Person;
import com.sy.mybatis.domain.Page;
import com.sy.util.Result;
import com.sy.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * @Author: shuYan
 * @Date: 2023/5/3 14:17
 * @Descript:
 */
@Controller
public class BaseController<T> {

    @Autowired
    private BaseService<T> baseService;

    @GetMapping("/get/{id}")
    public Result get(@PathVariable String id) {
        T result = baseService.getObject(id);
        if(result == null || result == "") {
            return ResultUtil.fail("没有当前id数据！");
        }
        return ResultUtil.success(result);
    }

    @PostMapping("/getList")
    public Result getList(@RequestBody Page page) {
        return ResultUtil.success(baseService.getList(page));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody T t) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String id = baseService.insert(t);
        return ResultUtil.success(id, "插入数据成功！");
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        String deleteId = baseService.delete(id);
        return ResultUtil.success(deleteId, "删除数据成功！");
    }

}
