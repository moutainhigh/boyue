package com.boyue.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.user.dto.UserDTO;
import com.boyue.user.entity.ByUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByUserService extends IService<ByUser> {

    /**
     * 数据校验：
     * 实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
     * 当用户注册时，会输入用户名或手机号，此时页面会异步请求服务端，
     * 服务端接收数据，校验数据的唯一性。
     *
     * @param data 要校验的数据
     * @param type 要校验的数据类型： 1.用户名；2.手机
     * @return true为可以用，false不可以用
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送短信验证码:
     * 用户输入手机号，点击发送验证码，前端会把手机号发送到服务端。
     * 服务端生成随机验证码，长度为6位，纯数字。并且调用短信服务，
     * 发送验证码到用户手机。
     * <p>
     * 步骤：
     * - 接收用户请求，手机号码
     * - 验证手机号格式
     * - 生成验证码
     * - 保存验证码到redis
     * - 发送RocketMQ消息到ly-sms
     *
     * @param phone 用户的手机号码
     */
    void sendSmsToPhone(String phone);

    /**
     * 用户注册：
     * 用户页面填写数据，发送表单到服务端，服务端实现用户注册功能。需要对用户密码进行加密存储，
     * 使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
     * 步骤：
     * - 验证短信验证码
     * - 生成言
     * - 密码加密
     * - 写入数据库
     *
     * @param user 注册用户对象
     * @param code   前端传过来的手机验证码
     */
    void register(ByUser user, String code);

    /**
     * 根据用户名和密码查询用户：
     * 查询功能，根据参数中的用户名和密码查询指定用户并且返回用户
     *
     * @param username 用户名
     * @param password 用户密码
     * @return userDTO对象
     */
    UserDTO findUserByUsernameAndPassword(String username, String password);
}
