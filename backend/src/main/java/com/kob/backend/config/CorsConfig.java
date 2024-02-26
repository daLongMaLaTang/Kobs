package com.kob.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // 当前跨域请求最大有效时长。这里默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    // 定义一个名为corsFilter的Bean，用于配置CORS过滤器
    @Bean
    public CorsFilter corsFilter() {
        // 创建基于URL的CORS配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 创建CORS配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址，允许所有来源访问
        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头，允许所有请求头
        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法，允许所有请求方法
        corsConfiguration.setMaxAge(MAX_AGE); // 设置最大有效时长

        // 将CORS配置对象注册到URL配置源中，/** 表示对所有接口配置跨域设置
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置

        // 创建CORS过滤器并返回
        return new CorsFilter(source);
    }
}
