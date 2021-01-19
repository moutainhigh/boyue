package com.boyue.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/17 00:07
 * @Author: Jacky
 * @Description: cors的配置
 * 使用SpringMVC提供的CorsFilter
 */
@Configuration
public class GlobalCorsConfig {

    @Autowired
    private CorsProperties prop;

    @Bean
    public CorsFilter corsFilter() {
        //1.添加cors的配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1.1 设置允许访问的域
        config.setAllowedOrigins(prop.getAllowedOrigins());
        //1.2 设置是否允许发送cookie
        config.setAllowCredentials(prop.getAllowedCredentials());
        //1.3 设置允许的请求方式
        config.setAllowedMethods(prop.getAllowedMethods());
        //1.4 设置允许的头信息
        config.setAllowedHeaders(prop.getAllowedHeaders());
        //1.5 设置访问有效期 单位秒
        config.setMaxAge(prop.getMaxAge());
        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(prop.getFilterPath(), config);
        //3.返回新的CORSFilter
        return new CorsFilter(source);
    }
}
