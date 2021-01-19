package com.boyue.upload.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 15:55
 * @Author: Jacky
 * @Description: 配置oss的client对象
 */
@Configuration
public class OSSConfig {
    @Bean
    public OSS ossClient(OSSProperties prop){
        return new OSSClientBuilder().build(prop.getEndpoint(), prop.getAccessKeyId(), prop.getAccessKeySecret());
    }
}
