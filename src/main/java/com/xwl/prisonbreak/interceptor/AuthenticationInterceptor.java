package com.xwl.prisonbreak.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.xwl.prisonbreak.aop.JWTCheckToken;
import com.xwl.prisonbreak.aop.JWTLoginToken;
import com.xwl.prisonbreak.common.util.JWTUtil;
import com.xwl.prisonbreak.michael.entity.SysUser;
import com.xwl.prisonbreak.michael.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author xwl
 * @date 2020-03-27 14:11
 * @description JWT拦截器拦截请求进行权限验证
 *
 * 实现一个拦截器就需要实现HandlerInterceptor接口
 *
 * HandlerInterceptor接口主要定义了三个方法：
 * 1.boolean preHandle ()：
 * 预处理回调方法,实现处理器的预处理，第三个参数为响应的处理器,自定义Controller,返回值为true表示继续流程（如调用下一个拦截器或处理器）或者接着执行
 * postHandle()和afterCompletion()；false表示流程中断，不会继续调用其他的拦截器或处理器，中断执行。
 *
 * 2.void postHandle()：
 * 后处理回调方法，实现处理器的后处理（DispatcherServlet进行视图返回渲染之前进行调用），此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
 *
 * 3.void afterCompletion():
 * 整个请求处理完毕回调方法,该方法也是需要当前对应的Interceptor的preHandle()的返回值为true时才会执行，也就是在DispatcherServlet渲染了对应的视图之后执行。用于进行资源清理。整个请求处理完毕回调方法。如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中
 *
 * 主要流程:
 * 1.从 http 请求头中取出 token，
 * 2.判断是否映射到方法
 * 3.检查是否有@JWTLoginToken注释，有（表示登录请求，登录请求不用拦截）则跳过认证
 * 4.检查有没有需要用户登录的注解@JWTCheckToken，有则需要取出并验证
 * 5.认证通过则可以访问，不通过会报相关错误信息
 *
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 检查是否有@JWTLoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(JWTLoginToken.class)) {
            JWTLoginToken loginToken = method.getAnnotation(JWTLoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }

        // 检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(JWTCheckToken.class)) {
            JWTCheckToken checkToken = method.getAnnotation(JWTCheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getClaim("id").asString();
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("访问异常！");
                }
                SysUser sysUser = sysUserService.findByIdUseAnnotation(userId);
                if (sysUser == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                Boolean verify = JWTUtil.isVerify(token, sysUser);
                if (!verify) {
                    throw new RuntimeException("非法访问！");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
