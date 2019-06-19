package com.xwl.prisonbreak.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: xwl
 * @Date: 2019/6/19 19:53
 * @Description: 自定义注解对应切面
 */
@Aspect
@Component
public class CustomAopAspect {
    @Around("@annotation(com.xwl.prisonbreak.aop.CustomAopAnnotation)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("方法开始时间是:" + new Date());
        // 获取目标方法的参数
        Object[] args = proceedingJoinPoint.getArgs();
        // **********以上均在目标方法执行之前执行*************
        // 执行标注有 @CustomAopAnnotation(param = "", detail = "")注解的方法
        Object obj = proceedingJoinPoint.proceed();
        // **********以下均在目标方法执行之前执行*************
        System.out.println("方法结束时间是:" + new Date());
        return obj;
    }
}
