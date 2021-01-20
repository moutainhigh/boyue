package com.boyue.common.utils.constants;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 21:33
 * @Author: Jacky
 * @Description: 正则表达式校验抽象类
 */

public abstract class RegexPatterns {
    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 用户名正则
     */
    public static final String USERNAME_REGEX = "^\\w{4,32}$";

}