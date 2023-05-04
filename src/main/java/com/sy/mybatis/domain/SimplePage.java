package com.sy.mybatis.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Date: 2022/2/15 20:09
 * @Author: shuYan
 * @ModifyTime:
 * @Description:
 */
@Data
public class SimplePage<T> implements Serializable {

    protected static final long serialVersionUID = 5136213157391895517L;

    protected int page = 1;// 页码，默认是第一页
    protected int rows = 10;// 每页显示的记录数，默认是10
    protected int totalRecord;// 总记录数
    protected List<T> data;// 对应的当前页记录

    public SimplePage() {}

    public SimplePage(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    /*public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List getData() {
        return data;
    }*/
    public SimplePage setData(List<T> data) {
        this.data = data;
        return this;
    }
}
