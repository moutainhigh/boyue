package com.boyue.auth.service;

import com.boyue.common.auth.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 18:46
 * @Author: Jacky
 * @Description: 授权中心的service接口
 */
public interface AuthService {
    /**
     * 用户从前端登录页面，输入用户名、密码，进行登录。
     *
     * @param username 用户名
     * @param password 用户密码
     * @param response 响应
     */
    void login(String username, String password, HttpServletResponse response);

    /**
     * 用户从前端登录成功后，前端携带cookie包含token，到服务端验证token有效性，并返回用户信息
     *
     * @param request  请求  用来获取token
     * @param response 响应
     * @return 用户信息
     */
    UserInfo verify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户退出操作
     *
     * @param request  请求  用来获取token
     * @param response 响应
     */
    void logout(HttpServletRequest request, HttpServletResponse response);
}
