package com.yxy.oa;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * API服务启动类
 **/
@SpringBootApplication
public class OaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OaApiApplication.class);
    }


    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new WebStatFilter());
        filter.setName("druidWebStatFilter");
        filter.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filter.addUrlPatterns("/*");
        return filter;
    }

    @Bean
    public ServletRegistrationBean getServletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servlet.setName("druidStatViewServlet");
        /**
         * IP白名单 (没有配置或者为空，则允许所有访问)
         */
        servlet.addInitParameter("allow", "");
        /**
         * IP黑名单 (存在共同时，deny优先于allow)
         */
        servlet.addInitParameter("deny", "");
        /**
         * 用户名
         */
        servlet.addInitParameter("loginUsername", "member");
        /**
         * 密码
         */
        servlet.addInitParameter("loginPassword", "member");
        /**
         * 禁用HTML页面上的“Reset All”功能
         */
        servlet.addInitParameter("resetEnable", "false");
        return servlet;
    }

}
