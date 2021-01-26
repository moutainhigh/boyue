package com.boyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 20:55
 * @Author: Jacky
 * @Description: 购物车微服务的启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }
}
