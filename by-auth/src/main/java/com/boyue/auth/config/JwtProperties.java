package com.boyue.auth.config;

import com.boyue.common.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PrivateKey;
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
     * 私钥地址
     */
    private String privateKeyPath;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    private UserTokenProperties user = new UserTokenProperties();

    /**
     * userToken的内部配置类
     */
    @Data
    public static class UserTokenProperties{
        //30  过期时间,单位分钟
        private Integer expire;
        //BY_TOKEN # cookie名称
        private String cookieName;
        // boyue.com # cookie的域
        private String cookieDomain;
        //minRefreshInterval: 15
        private Integer minRefreshInterval;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("publicKeyPath=={}",publicKeyPath);
        log.info("privateKeyPath=={}",privateKeyPath);

        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException(e);
        }
    }
}
