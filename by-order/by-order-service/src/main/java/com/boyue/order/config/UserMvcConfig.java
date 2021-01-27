package com.boyue.order.config;

import com.boyue.order.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 23:13
 * @Author: Jacky
 * @Description: 将自定义用户拦截器添加到springmvc拦截器中
 */
@Configuration
public class UserMvcConfig implements WebMvcConfigurer {

    /**
     * 将拦截用户获取token的拦截器添加进mvc拦截器内
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/order/**");
    }
}
