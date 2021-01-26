package com.boyue.zuul.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/17 00:06
 * @Author: Jacky
 * @Description: cors的配置类
 */
@Data
@ConfigurationProperties(prefix = "by.cors")
public class CorsProperties {
    private List<String> allowedOrigins;
    private Boolean allowedCredentials;
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private Long maxAge;
    private String filterPath;
}
