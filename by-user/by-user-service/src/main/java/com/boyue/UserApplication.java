package com.boyue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/24 11:10
 * @Author: Jacky
 * @Description: user的微服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient

@MapperScan("com.boyue.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
