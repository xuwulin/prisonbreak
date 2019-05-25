package com.xwl.prisonbreak.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @Auther: xuwulin
 * @Date: 2019/5/15 17:02
 * @Description: 阿里数据库连接处 Druid配置; 访问地址：http://localhost:8080/druid/index.html
 */
@Configuration
public class DruidConfig {

    // 将所有前缀为spring.datasource下的配置项都加载到DataSource中
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * druid的监控配置
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidStatViewServle() {

        ServletRegistrationBean srb = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 白名单
        srb.addInitParameter("allow", "127.0.0.1");

        // 黑名单
        srb.addInitParameter("deny", "");

        // 登陆账号
        srb.addInitParameter("loginUsername", "admin");
        srb.addInitParameter("loginPassword", "123456");
        // 是否重置数据
        srb.addInitParameter("resetEnable", "false");
        return srb;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean frb = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        frb.addUrlPatterns("/*");
        // 添加不需要忽略的格式信息
        frb.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return frb;
    }
}
