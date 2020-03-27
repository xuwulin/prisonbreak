package com.xwl.prisonbreak.config.jwt;

import com.xwl.prisonbreak.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xwl
 * @date 2020-03-27 13:39
 * @description 配置拦截器，使拦截器 AuthenticationInterceptor 生效
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 跨域请求
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")    // 允许任何域名使用
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许任何方法（post、get等）
                .maxAge(3600)
                .allowCredentials(true);
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**"); // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}