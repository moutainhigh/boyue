package com.boyue;

import com.boyue.zuul.config.CorsProperties;
import com.boyue.zuul.config.FilterProperties;
import com.boyue.zuul.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 21:33
 * @Author: Jacky
 * @Description: zuul网关的启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableConfigurationProperties({FilterProperties.class, JwtProperties.class, CorsProperties.class})
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
