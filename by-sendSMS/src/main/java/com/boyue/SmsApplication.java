package com.boyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 21:09
 * @Author: Jacky
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class,args);
    }
}
