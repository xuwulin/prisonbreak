package com.xwl.prisonbreak.interceptor;

import com.xwl.prisonbreak.aop.JWTCheckToken;
import com.xwl.prisonbreak.aop.JWTLoginToken;
import com.xwl.prisonbreak.common.Audience;
import com.xwl.prisonbreak.common.util.JWTUtil;
import com.xwl.prisonbreak.michael.service.SysUserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author xwl
 * @date 2020-03-27 14:11
 * @description JWT拦截器拦截请求进行权限验证
 * <p>
 * 实现一个拦截器就需要实现HandlerInterceptor接口
 * <p>
 * HandlerInterceptor接口主要定义了三个方法：
 * 1.boolean preHandle ()：
 * 预处理回调方法,实现处理器的预处理，第三个参数为响应的处理器,自定义Controller,返回值为true表示继续流程（如调用下一个拦截器或处理器）或者接着执行
 * postHandle()和afterCompletion()；false表示流程中断，不会继续调用其他的拦截器或处理器，中断执行。
 * <p>
 * 2.void postHandle()：
 * 后处理回调方法，实现处理器的后处理（DispatcherServlet进行视图返回渲染之前进行调用），此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
 * <p>
 * 3.void afterCompletion():
 * 整个请求处理完毕回调方法,该方法也是需要当前对应的Interceptor的preHandle()的返回值为true时才会执行，也就是在DispatcherServlet渲染了对应的视图之后执行。用于进行资源清理。整个请求处理完毕回调方法。如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中
 * <p>
 * 主要流程:
 * 1.从 http 请求头中取出 token，
 * 2.判断是否映射到方法
 * 3.检查是否有@JWTLoginToken注释，有（表示登录请求，登录请求不用拦截）则跳过认证
 * 4.检查有没有需要用户登录的注解@JWTCheckToken，有则需要取出并验证
 * 5.认证通过则可以访问，不通过会报相关错误信息
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private Audience audience;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求头信息Authorization信息
        final String authHeader = request.getHeader(JWTUtil.AUTH_HEADER_KEY);
        log.info("===>authHeader = {}", authHeader);

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
                if (authHeader == null || !authHeader.startsWith(JWTUtil.TOKEN_PREFIX)) {
                    log.info("===>用户未登录，请先登录");
                    throw new RuntimeException("用户未登录，请先登录");
                }

                // 获取token，authHeader.substring(7);的目的是去掉，我们添加的 Bearer (注意Bearer后有个空格，所以长度是7)
                final String token = authHeader.substring(7);

                if (audience == null) {
                    BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    audience = (Audience) factory.getBean("audience");
                }

                // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
                Claims claims = JWTUtil.parseJWT(token, audience.getSecret());
                if (claims == null) {
                    throw new LoginException("登录失败");
                }

                /*// 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(authHeader).getClaim("id").asString();
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("访问异常！");
                }
                SysUser sysUser = sysUserService.findByIdUseAnnotation(userId);
                if (sysUser == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                Boolean verify = JWTUtil.isVerify(authHeader, sysUser);
                if (!verify) {
                    throw new RuntimeException("非法访问！");
                }
                return true;*/
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
