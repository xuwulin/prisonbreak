package com.xwl.prisonbreak.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xwl.prisonbreak.annotation.AutoIdempotent;
import com.xwl.prisonbreak.common.util.JWTUtil;
import com.xwl.prisonbreak.common.util.RedisUtil;
import com.xwl.prisonbreak.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author xwl
 * @date 2020-04-15 9:47
 * @description 接口幂等拦截器
 */
@Component
public class AutoIdempotentInterceptor implements HandlerInterceptor {
    @Resource
    private RedisUtil redisUtil;

    /**
     * 预处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 被@AutoIdempotent注解标记的方法
        AutoIdempotent methodAnnotation = method.getAnnotation(AutoIdempotent.class);
        if (methodAnnotation != null) {
            try {
                return this.checkToken(request); // 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
            } catch (Exception ex) {
                // 可以在此记录日志
                throw ex;
            }
        }
        // 必须返回true,否则会被拦截一切请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 返回的json值
     *
     * @param response
     * @param json
     * @throws Exception
     */
    private void writeReturnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    /**
     * 验证header中是否存在token
     * @param request
     * @return
     * @throws Exception
     */
    public boolean checkToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader(JWTUtil.AUTH_HEADER_KEY);
        if (StrUtil.isBlank(token)) { // header中不存在token
            token = request.getParameter(JWTUtil.AUTH_HEADER_KEY);
            if (StrUtil.isBlank(token)) { // parameter中也不存在token
                throw new BusinessException("1003", "token缺失或者无效");
            }
        }

        if (!redisUtil.exists(token)) {
            throw new BusinessException("1023", "重复操作");
        }

        boolean remove = redisUtil.remove(token);
        if (!remove) {
            throw new BusinessException("1023", "重复操作");
        }
        return true;
    }
}
