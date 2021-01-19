package com.boyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 15:21
 * @Author: Jacky
 * @Description: 文件管理系统
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FastDfsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastDfsApplication.class, args);
    }
}
