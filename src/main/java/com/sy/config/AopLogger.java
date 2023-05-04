package com.sy.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * @Author: shuYan
 * @Date: 2023/5/2 10:20
 * @Descript: Spring AOP处理每个接口的操作日志
 */
@Aspect
@Slf4j
@Component
public class AopLogger {


    /**
     * 拦截当前项目所有的方法
     */
    @Pointcut("execution(* com.sy..*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            log.info("url: {}", request.getRequestURI());
        }
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        long endTime = System.currentTimeMillis();
        log.info("method: {}", method);
        log.info("total time: {}ms", (endTime - startTime));
        result = point.proceed(); // 异常抛出，给全局异常处理，但是会导致进入此函数两次(因为执行前进入报了异常，所以执行后再进入一次)
        log.info("method response: " + result); // 方法执行完后的返回结果
        return result;
    }

}
