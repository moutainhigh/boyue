package com.boyue.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/28 21:29
 * @Author: Jacky
 * @Description: 加载配置
 */
@Data
@ConfigurationProperties(prefix = "by.worker")
public class IdWorkerProperties {
    /**
     * 当前服务器id
     */
    private long workerId;

    /**
     * 序列号
     */
    private long dataCenterId;
}
