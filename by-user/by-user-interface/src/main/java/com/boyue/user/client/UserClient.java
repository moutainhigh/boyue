package com.boyue.user.client;

import com.boyue.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 18:32
 * @Author: Jacky
 * @Description: 用户的feignClient接口
 */
@FeignClient(value = "user-service")
public interface UserClient {
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
    @GetMapping(path = "/check/{data}/{type}", name = "数据校验")
    Boolean checkData(@PathVariable(name = "data") String data,
                      @PathVariable(name = "type") Integer type);

    /**
     * 根据用户名和密码查询用户：
     * 查询功能，根据参数中的用户名和密码查询指定用户并且返回用户
     * 路径接口：
     * GET /query
     *
     * @param username 用户名
     * @param password 用户密码
     * @return userDTO对象
     */
    @GetMapping(path = "/query", name = "根据参数中的用户名和密码查询指定用户")
    UserDTO findUserByUsernameAndPassword(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "password") String password);

}
