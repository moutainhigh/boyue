package com.boyue.user.config;

import com.boyue.user.interceptors.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 21:46
 * @Author: Jacky
 * @Description: 将自定义用户拦截器添加到springmvc拦截器中
 */
@Configuration
public class UserMvcConfig implements WebMvcConfigurer {
    /**
     * 重写添加拦截器方法
     * @param registry 参数
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/address/**").excludePathPatterns("/address/byUser/**");
    }
}
