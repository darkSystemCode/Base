package com.sy.config;

import com.sy.util.Result;
import com.sy.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 14:59
 * @Descript: 全局异常处理，从小到达，保证异常被正常捕获处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(NullPointerException.class)
    public Result nullPointerException(NullPointerException npe) {
        log.error("NullPointerException: {}", npe.getMessage());
        return ResultUtil.exception("空指针异常！");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result runtimeException(RuntimeException re) {
        log.error("RuntimeException: {}", re.getMessage());
        return ResultUtil.exception();
    }

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        log.error("Exception: {}", e.getMessage());
        return ResultUtil.exception();
    }
}
