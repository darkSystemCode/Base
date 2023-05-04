package com.sy.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 21:47
 * @Descript:
 */
@Slf4j
@Component
public class DataBase {

    private static String url;
    private static String username;
    private static String password;

    private static Connection connection = null;
    private static final ConcurrentHashMap<String, List<Column>> COLUMN_LIST = new ConcurrentHashMap();

    @Value("${spring.datasource.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${spring.datasource.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${spring.datasource.password}")
    public void setPassword(String password) {
        this.password = password;
    }
    
    public static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            log.info("mysql init success...");
        } catch (ClassNotFoundException e) {
            log.error("dataBase init error: {}", e);
        } catch (SQLException throwables) {
            log.error("dataBase init error: {}", throwables);
        }
    }

//    private static Connection connection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        return DriverManager.getConnection(url, username, password);
//    }

    public static List<Column> getColumns(String tableName) {
        if(ObjectUtils.isEmpty(tableName)) {
            return null;
        }
        // 如果缓存有直接在缓存拿，否则查询数据库系统表
        if(COLUMN_LIST.containsKey(tableName)) {
            return COLUMN_LIST.get(tableName);
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Column> columns = new ArrayList<>();
        try {
            String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=? AND TABLE_NAME=? ORDER BY ORDINAL_POSITION ASC";
            stmt = connection.prepareStatement(sql);
            String schema = null;
            if(!ObjectUtils.isEmpty(url)) {
                String[] split = url.split("/");
                if(split.length >= 2) {
                    if(split[3].contains("?")) {
                        schema = split[3].split("\\?")[0];
                    }
                }
            }
            if(!ObjectUtils.isEmpty(schema)) {
                stmt.setString(1, schema);
                stmt.setString(2, tableName);
                rs = stmt.executeQuery();
                while(rs.next()) {
                    columns.add(new Column(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE")));
                }
                COLUMN_LIST.put(tableName, columns);
            }
        } catch (Exception e) {
            log.error("dataBase error: {}", e);
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                if(rs != null) {
                    rs.close();
                }
                if(stmt != null) {
                    stmt.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                log.error("dataBase close error: {}", throwables);
            }
        }
        return columns;
    }

    /**
     * 通过表名和配置文件的数据库名获取表字段
     * @param tableName 表名
     * @return insert into tableName(field1, field2, fieldn) values(#{value1}, #{value2}, #{valuen})
     */
    public static String getInsert(String tableName) {
        if(ObjectUtils.isEmpty(tableName)) {
            return null;
        }
        List<Column> columns = getColumns(tableName);
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO");
        insert.append(" ");
        insert.append(tableName);
        insert.append("(");
        insert.append(columns.stream().map(item -> item.getColName()).collect(Collectors.joining(", ")));
        insert.append(") VALUES(");
        insert.append(columns.stream().map(item -> "#{entity." + item.getColName() + "}").collect(Collectors.joining(", ")));
        insert.append(")");
        return insert.toString();
    }

    /**
     * 通过表名和配置文件的数据库名获取表字段
     * @param tableName 表名
     * @return UPDATE TABLE_NAME SET field1=#{field1}, field2=#{field2},fieldn=#{fieldn} WHERE id=#{id}
     * 注意：id默认名是id，需要和BaseEntity的属性名id一致
     */
    public static String get(String tableName) {
        if(ObjectUtils.isEmpty(tableName)) {
            return null;
        }
        List<Column> columns = getColumns(tableName);
        StringBuilder update = new StringBuilder();
        update.append("UPDATE");
        update.append(" ");
        update.append(tableName);
        update.append(" ");
        update.append("SET");
        update.append(" ");
        update.append(columns.stream().map(col -> col.getColName() + "=#{" + col.getColName() + "}").collect(Collectors.joining(", ")));
        update.append(" ");
        update.append("WHERE");
        update.append(" ");
        update.append("id=#{id}");
        return update.toString();
    }

}
