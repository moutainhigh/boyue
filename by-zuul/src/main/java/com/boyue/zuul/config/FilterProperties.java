package com.boyue.zuul.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 20:23
 * @Author: Jacky
 * @Description: 白名单过滤器配置类
 */
@Data
@ConfigurationProperties(prefix = "by.filter")
public class FilterProperties {
    /**
     * 白名单内容
     */
    private List<String> allowPaths;

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }
}
