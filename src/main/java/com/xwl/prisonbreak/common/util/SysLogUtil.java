package com.xwl.prisonbreak.common.util;

import com.alibaba.fastjson.JSON;
import com.xwl.prisonbreak.annotation.CustomAopAnnotation;
import com.xwl.prisonbreak.common.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xwl
 * @date 2020-02-28 14:02
 * @description
 */
@Slf4j
public class SysLogUtil {

    public static SysLog getSysLog(JoinPoint joinPoint) {
        // 获取签名
        Signature signature = joinPoint.getSignature();
        // 目标方法所在的全类名：如com.xwl.prisonbreak.michael.controller.SysUserController
        String declaringTypeName = signature.getDeclaringTypeName();
        // 目标方法名：如findByIdUseXml
        String methodName = signature.getName();

        // 获取目标方法所在类上的@CustomAopAnnotation注解信息
        CustomAopAnnotation annotationOnClass = (CustomAopAnnotation) signature.getDeclaringType().getAnnotation(CustomAopAnnotation.class);
        if (annotationOnClass != null) {
            // 类上注解中的属性
            String param = annotationOnClass.param();
            String detail = annotationOnClass.detail();
        }

        // 从切面植入点处通过反射机制获取织入点处的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 目标方法：
        Method method = methodSignature.getMethod();

        // 获取目标方法上的@CustomAopAnnotation注解
        CustomAopAnnotation annotationOnMethod = method.getAnnotation(CustomAopAnnotation.class);
        if (annotationOnMethod != null) {
            // 方法注解中的属性
            String param2 = annotationOnMethod.param();
            String detail2 = annotationOnMethod.detail();
        }

        SysLog sysLog = new SysLog();
        sysLog.setMethod(declaringTypeName + "." + methodName + "()");

        // 请求方法中的参数
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                // ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                // ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                continue;
            }
            arguments[i] = args[i];
        }
        String params = (arguments == null || arguments.length == 0) ? null : JSON.toJSONString(arguments);
        sysLog.setParams(params);

        // 请求时间
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sysLog.setOperateDate(LocalDateTime.now().format(dtf));
        return sysLog;
    }

    public static SysLog getSysLog(JoinPoint joinPoint, Object result) {
        SysLog sysLog = getSysLog(joinPoint);
        sysLog.setResponse(JSON.toJSONString(result));
        return sysLog;
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }

        return ip;
    }
}
