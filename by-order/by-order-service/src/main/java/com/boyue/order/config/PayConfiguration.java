package com.boyue.order.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 17:12
 * @Author: Jacky
 * @Description: 微信属性注入
 */
@Configuration
public class PayConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "by.pay.wx")
    public WXPayConfigImpl payConfig(){

        return new WXPayConfigImpl();
    }

    /**
     * 注册WXPay对象
     * @param payConfig 支付相关配置
     * @return WXPay对象
     * @throws Exception 连结WX失败时用到
     */
    @Bean
    public WXPay wxPay(WXPayConfigImpl payConfig) throws Exception {
        return new WXPay(payConfig);
    }
}
