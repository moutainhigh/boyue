package com.boyue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 22:57
 * @Author: Jacky
 * @Description: 商品服务的启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.boyue.item.mapper")
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }
}
