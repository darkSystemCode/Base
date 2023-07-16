package com.sy.base;

import com.sy.core.DataBase;
import com.sy.mybatis.domain.Page;
import com.sy.mybatis.domain.SimplePage;
import com.sy.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 20:13
 * @Descript: 基础service类
 */
public class BaseService<T> {

    @Resource
    private BaseMapper baseMapper;

    public SimplePage getList(Page page) {
        List<T> rs = baseMapper.getList(page, Utils.getTableNameToLower(getClazz()));
        return page.setData(rs);
    }

    public T getObject(String id) {
        return (T) baseMapper.getObject(id, Utils.getTableNameToLower(getClazz()));
    }

    public String delete(String id) {
        baseMapper.delete(id, Utils.getTableNameToLower(getClazz()));
        return id;
    }

    public String insert(T data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 判断实体类data是否id为空，如果为空赋值，用于insert成功返回
        Class clazz = getClazz();
        String id = null;
        Method method = clazz.getSuperclass().getDeclaredMethod("getId");
        id = (String) method.invoke(data);
        if(ObjectUtils.isEmpty(id)) {
            // 新增
            id = Utils.getUUID();
            Method setId = clazz.getSuperclass().getDeclaredMethod("setId", String.class);
            setId.invoke(data, id);
        } else {
            // 修改
            this.delete(id);
        }

        // 通过实体类获取表名
        String tableName = Utils.getTableNameToLower(getClazz());
        // 通过表名和配置文件的数据库名获取表字段
        String insertStr = DataBase.getInsert(tableName);
        Integer insert = baseMapper.insert(data, insertStr);
        if(insert <= 0) {
            id = null;
        }
        return id;
    }

    public boolean insert(List<T> data) {
        return false;
    }

    /**
     * 通过当前类获取到泛型的实际类型
     *
     * @return 泛型实际class
     */
    private final Class getClazz() {
        return (Class<T>) ((ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }
}
