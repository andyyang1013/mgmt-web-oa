package com.yxy.oa.config;

import com.yxy.oa.config.convert.JsonMessageConverter;
import com.yxy.oa.config.filter.ApiInterceptor;
import com.yxy.oa.config.filter.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;


/**
 * springmvc基础配置类
 **/
@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 自定义消息转换器，用于统一封装响应数据。全部用json去处理
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(jsonMessageConverter());
    }

    @Bean
    public JsonMessageConverter jsonMessageConverter() {
        return new JsonMessageConverter();
    }


    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public ApiInterceptor apiInterceptor() {
        return new ApiInterceptor();
    }

    /**
     * 请求拦截配置
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有的请求，api文档暂时放过
        registry.addInterceptor(apiInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/data/import",
                        "/swagger/**",
                        "/swagger-resources/**"
                );
        //排除拦截的url:登录接口、登录状态接口；根据具体情况排除
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/data/import",
                        "/api/login",
                        "/api/loginDiff",
                        "/api/loginWithVerifyCode",
                        "/api/checkLoginStatus",
                        "/api/loginOut",
                        "/api/queryUserInfo",
                        "/api/queryUserInfoById",
                        "/api/exsistUser",
                        "/api/batchQueryUserInfo",
                        "/api/account/modifyPassword",
                        "/api/account/resetPassword",
                        "/api/account/resetPassByPhone",
                        "/api/account/resetPassByEmail",
                        "/api/register",
                        "/api/registerConfirm",
                        "/swagger/**",
                        "/swagger-resources/**"
                );
        super.addInterceptors(registry);
    }

    /**
     * 处理跨域问题
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");//允许域。若是所有：*
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
