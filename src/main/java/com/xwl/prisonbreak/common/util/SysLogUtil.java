package com.xwl.prisonbreak.common.util;

import com.alibaba.fastjson.JSON;
import com.xwl.prisonbreak.annotation.SysLogAnnotation;
import com.xwl.prisonbreak.common.Audience;
import com.xwl.prisonbreak.common.SysLog;
import io.swagger.annotations.ApiOperation;
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

/**
 * @author xwl
 * @date 2020-02-28 14:02
 * @description
 */
@Slf4j
public class SysLogUtil {

    public static SysLog getSysLog(JoinPoint joinPoint, Audience audience) {
        // 获取签名：SysUser com.xwl.prisonbreak.michael.controller.SysUserController.selectById(String)
        Signature signature = joinPoint.getSignature();
        // 目标方法所在的全类名：如com.xwl.prisonbreak.michael.controller.SysUserController
        String declaringTypeName = signature.getDeclaringTypeName();
        // 目标方法名：如findByIdUseXml
        String methodName = signature.getName();

        String description = "";
        // 获取目标方法所在类上的@CustomAopAnnotation注解信息
        SysLogAnnotation annotationOnClass = (SysLogAnnotation) signature.getDeclaringType().getAnnotation(SysLogAnnotation.class);
        if (annotationOnClass != null) {
            // 类上注解中的属性
            String param = annotationOnClass.param();
            String detail = annotationOnClass.detail();
            description = annotationOnClass.param();
        }

        // 从切面植入点处通过反射机制获取织入点处的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 目标方法：
        Method method = methodSignature.getMethod();

        // 获取目标方法上的@CustomAopAnnotation注解
        SysLogAnnotation annotationOnMethod = method.getAnnotation(SysLogAnnotation.class);
        if (annotationOnMethod != null) {
            // 方法注解中的属性
            String param2 = annotationOnMethod.param();
            String detail2 = annotationOnMethod.detail();
            description = annotationOnClass.param();
        }

        String operation = "";
        // 获取目标方法上的@ApiOperation注解
        ApiOperation apiOperationAnnotation = method.getAnnotation(ApiOperation.class);
        if (apiOperationAnnotation != null) {
            operation = apiOperationAnnotation.value();
        }

        // 获取HttpServletRequest对象，可以从中获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = getIpAddr(request);
        String url = request.getRequestURL().toString();
        String httpMethod = request.getMethod();

        // 从请求头中获取 Authentication
        String authentication = request.getHeader("Authorization");
        String username = "";
        if (authentication != null) {
            // 解析token
            authentication = authentication.replace("Bearer ", "");
            username = JWTUtil.getUsername(authentication, audience.getSecret());
        }

        SysLog sysLog = new SysLog();
        sysLog.setUsername(username);
        sysLog.setOperation(operation);
        sysLog.setDescription(description);
        sysLog.setIp(ip);
        sysLog.setUrl(url);
        sysLog.setHttpMethod(httpMethod);
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

        return sysLog;
    }

    public static SysLog getSysLog(JoinPoint joinPoint, Audience audience, Object result) {
        SysLog sysLog = getSysLog(joinPoint, audience);
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

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public String getCustomAopAnnotationDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(SysLogAnnotation.class).detail());
                    break;
                }
            }
        }
        return description.toString();
    }
}
