package com.boyue.cart.config;

import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:14
 * @Author: Jacky
 * @Description: cookie配置类
 */
@Data
//@ConfigurationProperties(prefix = "by.cart")
public class CookieProperties {
    private String cookieName;
}
