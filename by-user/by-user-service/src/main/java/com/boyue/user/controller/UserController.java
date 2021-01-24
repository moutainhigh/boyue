package com.boyue.user.controller;

import com.boyue.common.exception.ByException;
import com.boyue.user.entity.ByUser;
import com.boyue.user.service.ByUserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/24 20:30
 * @Author: Jacky
 * @Description: 用户中心的controller层
 */
@RestController
@Api("用户中心userController")
@Slf4j
public class UserController {

    /**
     * 注入用户中心的service
     */
    @Autowired
    private ByUserService userService;

    /**
     * 数据校验：
     * 实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
     * 当用户注册时，会输入用户名或手机号，此时页面会异步请求服务端，
     * 服务端接收数据，校验数据的唯一性。
     * 接口路径: GET /check/{data}/{type}
     *
     * @param data 要校验的数据
     * @param type 要校验的数据类型： 1.用户名；2.手机
     * @return true为可以用，false不可以用
     */
    @ApiOperation(value = "校验用户名数据是否可用，如果不存在则可用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data",value = "校验的数据",required = true,dataType = "String"),
            @ApiImplicitParam(name = "type",value = "要校验的数据类型： 1.用户名；2.手机",required = true,dataType = "Integer")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "校验结果有效，true或false代表可用或不可用"),
            @ApiResponse(code = 400, message = "请求参数有误，比如type不是指定值")
    })
    @GetMapping(path = "/check/{data}/{type}", name = "数据校验")
    public ResponseEntity<Boolean> checkData(@PathVariable(name = "data") String data,
                                             @PathVariable(name = "type") Integer type) {
        log.info("[by-user服务]checkData接口接收到请求,正在完成校验");
        return ResponseEntity.ok(userService.checkData(data, type));
    }

    /**
     * 发送短信验证码:
     * 用户输入手机号，点击发送验证码，前端会把手机号发送到服务端。
     * 服务端生成随机验证码，长度为6位，纯数字。并且调用短信服务，
     * 发送验证码到用户手机。
     * 接口路径:
     * POST /code
     * 步骤：
     * - 接收用户请求，手机号码
     * - 验证手机号格式
     * - 生成验证码
     * - 保存验证码到redis
     * - 发送RocketMQ消息到ly-sms
     *
     * @param phone 用户的手机号码
     * @return 空
     */
    @ApiOperation(value = "发送短信验证码")
    @ApiImplicitParam(name = "phone",value = "用户的手机号码",required = true,dataType = "String")
    @ApiResponses({
            @ApiResponse(code = 204,message = "请求接收"),
            @ApiResponse(code = 400,message = "参数有误"),
            @ApiResponse(code = 500,message = "服务器内部异常")
    })
    @PostMapping(path = "/code", name = "发送短信验证码")
    public ResponseEntity<Void> sendSmsToPhoneCode(@RequestParam(name = "phone") String phone) {
        log.info("[by-user服务]sendSmsToPhoneCode接口接收到请求,正在完成短信发送服务");
        userService.sendSmsToPhone(phone);
        return ResponseEntity.noContent().build();
    }

    /**
     * 用户注册：
     * 用户页面填写数据，发送表单到服务端，服务端实现用户注册功能。需要对用户密码进行加密存储，
     * 使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
     * 步骤：
     * - 验证短信验证码
     * - 生成言
     * - 密码加密
     * - 写入数据库
     * 接口路径：
     * POST /register
     *
     * @param user 注册用户对象
     * @param code   前端传过来的手机验证码
     * @return 空
     */
    @PostMapping(path = "/register", name = "用户注册")
    @ApiOperation(value = "发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "用户密码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "手机号",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "短信验证码",required = true,dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 201,message = "注册成功"),
            @ApiResponse(code = 400,message = "参数有误，注册失败"),
            @ApiResponse(code = 500,message = "服务器内部异常，注册失败")
    })
    public ResponseEntity<Void> register(@Valid ByUser user,
                                         BindingResult bindingResult,
                                         @RequestParam(name = "code") String code) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            String errorMsg = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(errorMsg)) {
                log.info("[by-user服务]register接口接收到请求,参数不合法");
                throw new ByException(HttpStatus.BAD_REQUEST.value(), errorMsg);
            }
        }

        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
