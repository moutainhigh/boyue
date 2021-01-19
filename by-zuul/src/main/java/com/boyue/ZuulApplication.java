package com.boyue;

import com.boyue.zuul.config.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 21:33
 * @Author: Jacky
 * @Description: zuul网官的启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableConfigurationProperties(CorsProperties.class)
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
