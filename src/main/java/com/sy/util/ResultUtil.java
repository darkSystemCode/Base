package com.sy.util;

/**
 * @Author: shuYan
 * @Date: 2023/5/1 23:32
 * @Descript: 返回结果工具类，分为三个等级success、fail、exception
 */
public class ResultUtil {

    public static <T> Result success(T data, Integer code, String message) {
        return new Result(data, code, message);
    }

    public static <T> Result success(T data, String message) {
        return new Result(data, ResultEnum.SUCCESS.code, message);
    }

    public static <T> Result success(T data, Integer code) {
        return new Result(data, code, ResultEnum.SUCCESS.message);
    }

    public static <T> Result success(T data) {
        return new Result(data, ResultEnum.SUCCESS.code, ResultEnum.SUCCESS.message);
    }

    public static <T> Result success() {
        return new Result(ResultEnum.SUCCESS.code, ResultEnum.SUCCESS.message);
    }

    public static <T> Result fail(T data, Integer code, String message) {
        return new Result(data, code, message);
    }

    public static <T> Result fail(T data, String message) {
        return new Result(data, ResultEnum.FAIL.code, message);
    }

    public static <T> Result fail( String message) {
        return new Result(ResultEnum.FAIL.code, message);
    }

    public static <T> Result fail(T data, Integer code) {
        return new Result(data, code, ResultEnum.FAIL.message);
    }

    public static <T> Result fail(T data) {
        return new Result(data, ResultEnum.FAIL.code, ResultEnum.FAIL.message);
    }

    public static <T> Result fail() {
        return new Result(ResultEnum.FAIL.code, ResultEnum.FAIL.message);
    }

    public static <T> Result exception(T data, Integer code, String message) {
        return new Result(data, code, message);
    }

    public static <T> Result exception(T data, String message) {
        return new Result(data, ResultEnum.EXCEPTION.code, message);
    }

    public static <T> Result exception(T data, Integer code) {
        return new Result(data, code, ResultEnum.EXCEPTION.message);
    }

    public static <T> Result exception(T data) {
        return new Result(data, ResultEnum.EXCEPTION.code, ResultEnum.EXCEPTION.message);
    }

    public static <T> Result exception() {
        return new Result(ResultEnum.EXCEPTION.code, ResultEnum.EXCEPTION.message);
    }

    /**
     * 返回集枚举类，默认是成功（1），失败（0），异常（-1）
     */
    private enum ResultEnum {
        SUCCESS(1, "成功"),
        FAIL(0, "失败"),
        EXCEPTION(-1, "系统异常");

        private Integer code;
        private String message;

        ResultEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public static void main(String[] args) {
        System.out.println(ResultUtil.success("测试消息"));
        System.out.println(ResultUtil.success("测试消息", 200));
        System.out.println(ResultUtil.fail("系统错误"));
        System.out.println(ResultUtil.exception("系统异常"));
    }

}
