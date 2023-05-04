package com.sy.util;

import lombok.Data;

/**
 * 系统返回集对象
 *
 * @param <T> data: 返回数据集
 *            String message: 返回消息
 *            Integer code: 返回代码
 */
@Data
public class Result<T> {

    private T data;
    private String message;
    private Integer code;

    public Result(T data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message) {
        this.data = null;
        this.code = code;
        this.message = message;
    }

}
