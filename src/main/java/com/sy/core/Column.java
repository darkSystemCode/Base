package com.sy.core;

import lombok.Data;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 22:19
 * @Descript:
 */
@Data
public class Column {
    private String colName;
    private String colType;

    public Column() {}

    public Column(String colName, String colType) {
        this.colName = colName;
        this.colType = colType;
    }
}
