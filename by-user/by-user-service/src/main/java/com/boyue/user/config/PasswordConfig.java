package com.boyue.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/24 23:21
 * @Author: Jacky
 * @Description: BcryptPassWordEncode配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "by.encoder.crypt")
public class PasswordConfig {
    /**
     * 随机的密钥，使用uuid
     */
    private int strength;
    /**
     * 加密强度,密码和盐加密时的运算次数
     */
    private String secret;

    /**
     * 初始化bean
     * @return 实体对象
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        SecureRandom random = new SecureRandom(secret.getBytes());
        return new BCryptPasswordEncoder(strength,random);
    }
}
