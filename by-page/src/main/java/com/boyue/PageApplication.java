package com.boyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 11:32
 * @Author: Jacky
 * @Description: 商品详细页的微服务启动类
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class PageApplication {
    public static void main(String[] args) {
        SpringApplication.run(PageApplication.class, args);
    }
}
