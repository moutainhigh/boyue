package com.boyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 20:24
 * @Author: Jacky
 * @Description: 定时任务微服务的启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class,args);
    }
}
