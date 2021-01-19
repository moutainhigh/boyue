package com.boyue.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 15:54
 * @Author: Jacky
 * @Description: 加载oss的配置文件
 */
@Data
@Component
@ConfigurationProperties(prefix = "by.oss")
public class OSSProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
    private String host;
    private String endpoint;
    private String dir;
    private long expireTime;
    private long maxFileSize;
}
