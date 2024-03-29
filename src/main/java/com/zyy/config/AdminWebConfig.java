package com.zyy.config;

import com.zyy.Interceptor.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");

        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")//所有请求都被拦截包括静态资源
                .excludePathPatterns("/zyy/email/getEmailCode")
                .excludePathPatterns("/zyy/user/accountLogin").excludePathPatterns("/zyy/user/register").excludePathPatterns("/zyy/user/emailLogin")
                .excludePathPatterns("/zyy/company/accountLogin").excludePathPatterns("/zyy/company/emailLogin").excludePathPatterns("/zyy/company/register")
                .excludePathPatterns("/zyy/password/resetPassword");//放行的请求
    }

}
