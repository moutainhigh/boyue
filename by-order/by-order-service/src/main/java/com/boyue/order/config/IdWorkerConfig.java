package com.boyue.order.config;

import com.boyue.common.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/28 21:29
 * @Author: Jacky
 * @Description: 创建雪花算法的工具类
 */
@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {
    /**
     * 初始化bean
     * @param prop 配置类对象
     * @return 初始化后的对象
     */
    @Bean
    public IdWorker idWorker(IdWorkerProperties prop) {
        return new IdWorker(prop.getWorkerId(), prop.getDataCenterId());
    }
}
