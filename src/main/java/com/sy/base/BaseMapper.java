package com.sy.base;

import com.sy.entity.Person;
import com.sy.mybatis.domain.Page;
import com.sy.mybatis.domain.SimplePage;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 20:04
 * @Descript: 基础数据库类，选用注解式SQL，可以不用考虑XML的resultType问题
 */
public interface BaseMapper<T> {

    /**
     * 获取全部对象
     * @return
     */
    @Select("SELECT * FROM ${tableName}")
    List<T> getList(Page page, String tableName);

    /**
     * 获取单个对象
     * @return
     */
    @Select("SELECT * FROM ${tableName} WHERE ID=#{id}")
    T getObject(T id, String tableName);


    /**
     * 通过id去删除对象，物理删除
     * @param id
     * @return
     */
    @Delete("DELETE FROM ${tableName} WHERE id=#{id}")
    Integer delete(String id, String tableName);

    /**
     * 插入一个对象，并返回插入的对象id
     * 废除update方法，使用insert方法作为修改，如果入参有id那么就是修改，先通过id删除，再新增，否则，赋值id新增，这样从一定程度上保证了dataBase数据唯一性
     * @param entity
     * @return
     */
    @Insert("${insertStr}")
    Integer insert(@Param("entity") T entity, @Param("insertStr") String insertStr);

    /**
     * 批量插入
     * @param data
     * @return
     */
    boolean insert(List<T> data);
}
