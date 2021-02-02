package com.boyue.auth.controller;

import com.boyue.auth.dto.AdminUserDTO;
import com.boyue.auth.service.AuthService;
import com.boyue.common.auth.entity.UserInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 18:45
 * @Author: Jacky
 * @Description: 授权中心的controller层接口
 */
@RestController
@Api("授权中心AuthController")
@Slf4j
public class AuthController {

    /**
     * 注入权限的service层
     */
    @Autowired
    private AuthService authService;

    /**
     * 用户从前端登录页面，输入用户名、密码，进行登录。
     * POST /login
     *
     * @param username 用户名
     * @param password 用户密码
     * @param response 响应
     * @return 空
     */
    @ApiOperation(value = "用户从前端登录页面,进行登录。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String")
    })
    @ApiResponse(code = 200, message = "登陆成功")
    @PostMapping(path = "/login", name = "进行登录")
    public ResponseEntity<Void> login(@RequestParam(name = "username") String username,
                                      @RequestParam(name = "password") String password,
                                      HttpServletResponse response) {
        log.info("[auth-service服务]login接口处理请求");
        authService.login(username, password, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * 管理系统的登录功能，输入用户名、密码，进行登录。
     * POST /login
     *
     * @param adminUserDTO 用户
     * @param response 响应
     * @return 空
     */
    @ApiOperation(value = "管理系统的登录功能,进行登录。")
    @ApiResponse(code = 200, message = "登陆成功")
    @PostMapping(path = "/adminLogin", name = "管理系统的登录功能，进行登录")
    public ResponseEntity<Void> adminLogin(@RequestBody AdminUserDTO adminUserDTO,
                                           HttpServletResponse response) {
        log.info("[auth-service服务]adminLogin接口处理请求");
        authService.adminLogin(adminUserDTO.getUsername(), adminUserDTO.getPassword(), response);
        return ResponseEntity.noContent().build();
    }

    /**
     * 用户从前端登录成功后，前端携带cookie包含token，到服务端验证token有效性，并返回用户信息
     * 接口路径   GET /verify
     *
     * @param request  请求  用来获取token
     * @param response 响应
     * @return 用户信息
     */
    @ApiOperation(value = "校验登录用户的token")
    @GetMapping(path = "/verify", name = "校验登录用户的token")
    public ResponseEntity<UserInfo> verify(HttpServletRequest request, HttpServletResponse response) {
        log.info("[auth-service服务]verify接口处理请求，校验用户token");
        return ResponseEntity.ok(authService.verify(request, response));
    }

    /**
     * 用户退出操作
     * 接口路径   POST /logout
     *
     * @param request  请求  用来获取token
     * @param response 响应
     * @return 空
     */
    @PostMapping(path = "/logout", name = "用户退出操作")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("[auth-service服务]logout接口处理请求，退出登录");
        authService.logout(request, response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
