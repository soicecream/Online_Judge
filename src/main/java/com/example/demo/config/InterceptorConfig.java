package com.example.demo.config;

import com.example.demo.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**") // 拦截所有请求，判断通过token是否合法决定是否需要登录
                .excludePathPatterns("/user/login", "/user/register", "/**/export", "/**import", "/files/**",
                        "/swagger-resources/**", "/swagger-ui.html/**", "/**/importUserList");
//                         "**/swagger-ui/**", "**/doc.html/**",
//
//                        , "/**/export", "/**/import"
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}
