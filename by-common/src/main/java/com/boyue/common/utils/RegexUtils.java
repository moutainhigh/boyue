package com.boyue.common.utils;

import com.boyue.common.utils.constants.RegexPatterns;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 21:33
 * @Author: Jacky
 * @Description: 正则表达式校验工具类
 */
public class RegexUtils {
    /**
     * 是否符合手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhone(String phone){
        return matches(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * 是否符合邮箱格式
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmail(String email){
        return matches(email, RegexPatterns.EMAIL_REGEX);
    }

    private static boolean matches(String str, String regex){
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches(regex);
    }
}
