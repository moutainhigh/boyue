package com.boyue.cart.interceptors;

import com.boyue.common.auth.entity.Payload;
import com.boyue.common.auth.entity.UserInfo;
import com.boyue.common.auth.utils.JwtUtils;
import com.boyue.common.threadlocals.UserHolder;
import com.boyue.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:09
 * @Author: Jacky
 * @Description: 自定义springMVC拦截器,拦截请求，获取用户id,存储到userThreadLocal中
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    private static final String COOKIE_NAME = "BY_TOKEN";

    /**
     * 开始拦截用户请求
     * 获取cookie中的token
     * 解析token
     * 从token中获取userId
     *
     * @param request 请求
     * @param response 响应
     * @param handler 传递消息Message
     * @return 判断结果
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            //获取cookie中的token
            String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
            //解析token获取userId
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, UserInfo.class);
            Long userId = payload.getUserInfo().getId();
            log.info("获取到用户id，{}",userId);
            //赋值给threadLocal
            log.info("[User拦截器]将获取的userId赋值给threadLocal");
            UserHolder.setUserId(userId);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("[User拦截器]解析用户数据信息失败");
            return false;
        }
    }

    /**
     * 结束删除threadLocal中的userId
     * 防止内存泄露，内存溢出，导致OOM
     * @param request 请求
     * @param response 响应
     * @param handler 传递消息Message
     * @param ex 异常
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("[User拦截器]结束前，从threadLocal中删除userId");
        UserHolder.removeUserId();
    }
}
