package com.boyue.zuul.config;

import com.boyue.common.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PublicKey;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 18:41
 * @Author: Jacky
 * @Description:
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "by.jwt")
public class JwtProperties implements InitializingBean {
    /**
     * 公钥地址
     */
    private String publicKeyPath;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    private UserTokenProperties user = new UserTokenProperties();

    /**
     * userToken的内部配置类
     */
    @Data
    public static class UserTokenProperties{
        /**
         * 存放token的cookie名称
         */
        private String cookieName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("publicKeyPath=={}",publicKeyPath);

        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥失败！", e);
            throw new RuntimeException(e);
        }
    }
}
