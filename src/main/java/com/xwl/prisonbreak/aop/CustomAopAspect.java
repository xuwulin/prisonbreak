package com.xwl.prisonbreak.aop;

import com.alibaba.fastjson.JSON;
import com.xwl.prisonbreak.common.SysLog;
import com.xwl.prisonbreak.common.util.SysLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Auther: xwl
 * @Date: 2019/6/19 19:53
 * @Description: 自定义注解对应切面
 */
@Slf4j
@Aspect
@Component
public class CustomAopAspect {

    /**
     * @CustomAopAnnotation 加在类上
     *
     * @Around("@within(com.xwl.prisonbreak.annotation.CustomAopAnnotation)")
     * 和 @annotation("@within(com.xwl.prisonbreak.annotation.CustomAopAnnotation)")的区别：
     *
     * 注：@within 用于切类，即@CustomAopAnnotation注解使用在类上，该类中的每个方法都会被切入
     *    @annotation 是用于切方法，即@CustomAopAnnotation注解使用在方法上
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(com.xwl.prisonbreak.annotation.CustomAopAnnotation)")
    public Object aroundOnClass(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        SysLog sysLog = SysLogUtil.getSysLog(proceedingJoinPoint);

        // 异常信息
        String exception = null;
        // 目标方法的返回值
        Object obj = null;
        try {
            // 执行标注有 @CustomAopAnnotation(param = "", detail = "")注解的方法
            obj = proceedingJoinPoint.proceed();
            sysLog.setResponse(JSON.toJSONString(obj));
        } catch (Throwable throwable) {
            exception = JSON.toJSONString(throwable);
            throw throwable;
        } finally {
            if (exception != null) {
                sysLog.setException(exception);
                log.error(JSON.toJSONString(sysLog));
            } else {
                log.info(JSON.toJSONString(sysLog));
            }
        }
        return obj;
    }

    /**
     * @CustomAopAnnotation 加在方法上
     *
     * @Around("@within(com.xwl.prisonbreak.annotation.CustomAopAnnotation)")
     * 和 @annotation("@within(com.xwl.prisonbreak.annotation.CustomAopAnnotation)")的区别：
     *
     * 注：@within 用于切类，即@CustomAopAnnotation注解使用在类上，该类中的每个方法都会被切入
     *    @annotation 是用于切方法，即@CustomAopAnnotation注解使用在方法上
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.xwl.prisonbreak.annotation.CustomAopAnnotation)")
    public Object aroundOnMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        SysLog sysLog = SysLogUtil.getSysLog(proceedingJoinPoint);

        // 异常信息
        String exception = null;
        // 目标方法的返回值
        Object obj = null;
        try {
            // 执行标注有 @CustomAopAnnotation(param = "", detail = "")注解的方法
            obj = proceedingJoinPoint.proceed();
            sysLog.setResponse(JSON.toJSONString(obj));
        } catch (Throwable throwable) {
            exception = JSON.toJSONString(throwable);
            throw throwable;
        } finally {
            if (exception != null) {
                sysLog.setException(exception);
                log.error(JSON.toJSONString(sysLog));
            } else {
                log.info(JSON.toJSONString(sysLog));
            }
        }
        return obj;
    }
}
