package com.sy.mybatis.interceptor;

import com.sy.mybatis.domain.SimplePage;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.springframework.util.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Date: 2022/2/15 20:10
 * @Author: shuYan
 * @ModifyTime:
 * @Description: mybatis分页插件
 */
@Slf4j
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class , Integer.class}) })
public class PaginationInterceptor implements Interceptor {

//    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
//    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
//    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private static String dialect = "mysql";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获得拦截的对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 待执行的sql的包装对象
        BoundSql boundSql = statementHandler.getBoundSql();
        // 判断是否是查询语句
        if (isSelect(boundSql.getSql())) {
            // 获得参数集合
            Object params = boundSql.getParameterObject();

            if (params instanceof Map) { // 请求为多个参数，参数采用Map封装
                return complexParamsHandler(invocation, boundSql, (Map<?, ?>) params);
            } else if (params instanceof SimplePage) { // 单个参数且为Page，则表示该操作需要进行分页处理
                return pageHandlerExecutor(invocation, boundSql, (SimplePage) params);
            }
        }
        return invocation.proceed();
    }

    private Object complexParamsHandler(Invocation invocation, BoundSql boundSql, Map<?, ?> params) throws Throwable {
        //判断参数中是否指定分页
        if (containsPage(params)) {
            return pageHandlerExecutor(invocation, boundSql, (SimplePage) params.get("page"));
        } else {
            return invocation.proceed();
        }
    }

    private boolean containsPage(Map<?, ?> params) {
        if(params == null){
            return false;
        }
        if(!params.containsKey("page")){
            return false;
        }
        Object page = params.get("page");
        if(page == null){
            return false;
        }
        if(page instanceof SimplePage){
            return true;
        }
        return false;
    }

    private boolean isSelect(String sql) {
        if (!ObjectUtils.isEmpty(sql) && sql.toUpperCase().trim().startsWith("SELECT")) {
            return true;
        }
        return false;
    }

//    private Object simpleParamHandler(Invocation invocation, BoundSql boundSql, SimplePage page) throws Throwable {
//        return pageHandlerExecutor(invocation, boundSql, page);
//    }

    private Object pageHandlerExecutor(Invocation invocation, BoundSql boundSql, SimplePage page) throws Throwable {
        // 获得数据库连接
        Connection connection = (Connection) invocation.getArgs()[0];
        // 使用Mybatis提供的MetaObject
        MetaObject statementHandler = SystemMetaObject.forObject(invocation.getTarget());
//        MetaObject statementHandler = MetaObject.forObject(invocation.getTarget(), DEFAULT_OBJECT_FACTORY,
//                DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

        // 获取该sql执行的结果集总数
        int maxSize = getTotalSize(connection, (MappedStatement) statementHandler.getValue("delegate.mappedStatement"),
                boundSql);

        // 生成分页sql
        page.setTotalRecord(maxSize);
        String wrapperSql = getPageSql(boundSql.getSql(), page);


        MetaObject boundSqlMeta = SystemMetaObject.forObject(boundSql);
//        MetaObject boundSqlMeta = MetaObject.forObject(boundSql, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
//                DEFAULT_REFLECTOR_FACTORY);
        // 修改boundSql的sql
        boundSqlMeta.setValue("sql", wrapperSql);
        Object sql = boundSqlMeta.getValue("sql");
        log.info("sql info：{}", sql);
        return invocation.proceed();
    }

    private int getTotalSize(Connection connection, MappedStatement mappedStatement, BoundSql boundSql) {
        String countSql = getCountSql(boundSql.getSql());
        PreparedStatement countStmt;
        ResultSet rs;
        List<AutoCloseable> closeableList = new ArrayList<AutoCloseable>();

        try {
            countStmt = connection.prepareStatement(countSql);
//            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
//                    boundSql.getParameterMappings(), boundSql.getParameterObject());
//            setParameters(countStmt, mappedStatement, boundSql, boundSql.getParameterObject());
            new DefaultParameterHandler(mappedStatement, boundSql, boundSql).setParameters(countStmt);
            rs = countStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            closeableList.add(countStmt);
            closeableList.add(rs);
        } catch (SQLException e) {
            log.error("append an exception[{}] when execute sql[{}] with {}", e, countSql,
                    boundSql.getParameterObject());
        } finally {
            for (AutoCloseable closeable : closeableList) {
                try {
                    if (closeable != null)
                        closeable.close();
                } catch (Exception e) {
                    log.error("append an exception[{}] when close resource[{}] ", e, closeable);
                }
            }
        }
        return 0;
    }

//    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
//                               Object parameterObject) throws SQLException {
//        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
//        parameterHandler.setParameters(ps);
//    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {}

    public String getCountSql(String sql) {
        if("mysql".equals(dialect)){
            return "select count(0) from (" + sql + ") as total";
        }
        return sql;
    }

    public String getPageSql(String sql, SimplePage page) {
        if(page.getPage() <= 0){
            page.setPage(1);
        }
        if(page.getRows() <= 0){
            page.setRows(10);
        }
        int startRow = (page.getPage() - 1) * page.getRows();

        if(startRow >= page.getTotalRecord()){
            page.setPage(1);
            startRow=0;
        }
        if("mysql".equals(dialect)){
            return sql+" limit "+startRow+", "+page.getRows();
        }
        return sql;
    }
}
