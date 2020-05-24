package com.xwl.prisonbreak.aop;

import com.alibaba.fastjson.JSON;
import com.xwl.prisonbreak.common.Audience;
import com.xwl.prisonbreak.common.SysLog;
import com.xwl.prisonbreak.common.util.SysLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Auther: xwl
 * @Date: 2019/6/19 19:53
 * @Description: 自定义注解对应切面
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private Audience audience;

    /**
     * @SysLogAnnotation 加在类上
     *
     * @Around("@within(com.xwl.prisonbreak.annotation.SysLogAnnotation)")
     * 和 @annotation("@within(com.xwl.prisonbreak.annotation.CustomAopAnnotation)")的区别：
     *
     * 注：@within 用于切类，即@SysLogAnnotation注解使用在类上，该类中的每个方法都会被切入
     *    @annotation 是用于切方法，即@SysLogAnnotation注解使用在方法上
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(com.xwl.prisonbreak.annotation.SysLogAnnotation)")
    public Object aroundOnClass(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return process(proceedingJoinPoint);
    }

    /**
     * @SysLogAnnotation 加在方法上
     *
     * @Around("@within(com.xwl.prisonbreak.annotation.SysLogAnnotation)")
     * 和 @annotation("@within(com.xwl.prisonbreak.annotation.SysLogAnnotation)")的区别：
     *
     * 注：@within 用于切类，即@SysLogAnnotation注解使用在类上，该类中的每个方法都会被切入
     *    @annotation 是用于切方法，即@SysLogAnnotation注解使用在方法上
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.xwl.prisonbreak.annotation.SysLogAnnotation)")
    public Object aroundOnMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return process(proceedingJoinPoint);
    }

    private Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        SysLog sysLog = SysLogUtil.getSysLog(proceedingJoinPoint, audience);
        // 请求时间
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sysLog.setOperateDate(LocalDateTime.now().format(dtf));

        // 异常信息
        String exception = null;
        // 目标方法的返回值
        Object obj;
        try {
            // 执行标注有 @SysLogAnnotation(param = "", detail = "")注解的方法
            obj = proceedingJoinPoint.proceed();
            sysLog.setResponse(JSON.toJSONString(obj));
        } catch (Throwable throwable) {
            exception = JSON.toJSONString(throwable);
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            // 请求耗时
            sysLog.setTimeConsuming((endTime - startTime) + "ms");
            if (exception != null) {
                sysLog.setException(exception);
            }
            log.info(JSON.toJSONString(sysLog));
        }
        return obj;
    }
}
