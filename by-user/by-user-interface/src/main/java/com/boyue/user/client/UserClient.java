package com.boyue.user.client;

import com.boyue.user.dto.UserAddressDTO;
import com.boyue.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 18:32
 * @Author: Jacky
 * @Description: 用户的feignClient接口
 */
@FeignClient("user-service")
public interface UserClient {
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
    @GetMapping(path = "/query", name = "根据用户名和密码查询用户")
    UserDTO findUserByUsernameAndPassword(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "password") String password);

    /**
     * 根据 主键id 查询收货人地址信息
     * GET /address/byId
     * @param id 地址id
     * @return 用户地址对象
     */
    @GetMapping(path = "/address/byId",name = "根据 主键id 查询收货人地址信息")
    UserAddressDTO findUserAddressById(@RequestParam(name = "id") Long id);
}
