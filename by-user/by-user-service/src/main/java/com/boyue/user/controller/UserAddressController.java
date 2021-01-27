package com.boyue.user.controller;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.threadlocals.UserHolder;
import com.boyue.common.utils.BeanHelper;
import com.boyue.user.dto.UserAddressDTO;
import com.boyue.user.entity.ByUserAddress;
import com.boyue.user.service.ByUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 20:13
 * @Author: Jacky
 * @Description: 用户地址的controller层接口
 */
@Api("用户地址中心UserAddressController")
@Slf4j
@RestController
public class UserAddressController {

    /**
     * 注入userAddressService
     */
    @Autowired
    private ByUserAddressService userAddressService;

    /**
     * 查询收货人地址信息
     * GET /address/byId
     *
     * @param id 地址id
     * @return 用户地址对象
     */
    @ApiOperation("查询收货人地址信息")
    @GetMapping(path = "/address/byId", name = "查询收货人地址信息")
    public ResponseEntity<UserAddressDTO> findUserAddressByAddId(@RequestParam(name = "id") Long id) {
        log.info("[by-user服务]findUserAddressByAddId接口接收到请求,查询收货人地址信息");
        UserAddressDTO userAddressDTO = userAddressService.findUserAddressByAddId(id);
        return ResponseEntity.ok(userAddressDTO);
    }

    /**
     * 根据用户id获取用户地址信息
     * GET /address?userId=123&id=123
     *
     * @param userId 用户id
     * @param id     地址id
     * @return 用户地址对象
     */
    @ApiOperation("根据用户id获取用户地址信息")
    @GetMapping(path = "/address", name = "根据用户id获取用户地址信息")
    public ResponseEntity<UserAddressDTO> findUserAddressByUid(@RequestParam(name = "userId") Long userId,
                                                               @RequestParam(name = "id") Long id) {
        log.info("[by-user服务]findUserAddressByUid接口接收到请求,根据用户id获取用户地址信息");
        UserAddressDTO userAddressDTO = userAddressService.findUserAddressByUid(id, userId);
        return ResponseEntity.ok(userAddressDTO);
    }

    /**
     * 查询用户的所有收货信息
     * get /address/list
     *
     * @return 用户地址对象的list集合
     */
    @ApiOperation("查询用户的所有收货信息")
    @GetMapping(path = "/address/list", name = "查询用户的所有收货信息")
    public ResponseEntity<List<UserAddressDTO>> findUserAddressList() {
        log.info("[by-user服务]findAddressList接口接收到请求,根据用户id获取用户地址信息");
        return ResponseEntity.ok(userAddressService.findUserAddressList());
    }

    /**
     * 新增用户收货地址
     * post /address/save
     *
     * @param userAddressDTO 用户地址对象
     * @return 空
     */
    @ApiOperation("新增用户收货地址")
    @PostMapping(path = "/address/save", name = "新增用户收货地址")
    public ResponseEntity<Void> saveUserAddress(@RequestBody UserAddressDTO userAddressDTO) {
        log.info("[by-user服务]saveUserAddress接口接收到请求,新增用户收货地址");
        if (userAddressDTO == null) {
            log.error("[by-user服务]saveUserAddress接口参数不合法");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        Long userId = UserHolder.getUserId();
        ByUserAddress userAddress = BeanHelper.copyProperties(userAddressDTO, ByUserAddress.class);
        //设置用户id
        userAddress.setUserId(userId);
        //不是默认地址
        userAddress.setIsDefault(false);
        boolean flag = userAddressService.save(userAddress);
        if (!flag) {
            log.error("[by-user服务]saveUserAddress接口新增操作失败");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改用户收货地址
     * put /address/update
     *
     * @param userAddressDTO 用户地址对象
     * @return 空
     */
    @ApiOperation("修改用户收货地址")
    @PutMapping(path = "/address/update", name = "修改用户收货地址")
    public ResponseEntity<Void> updateUserAddress(@RequestBody UserAddressDTO userAddressDTO) {
        log.info("[by-user服务]updateUserAddress接口接收到请求,修改用户收货地址");
        //验证数据
        if (userAddressDTO == null) {
            log.error("[by-user服务]updateUserAddress接口参数不合法");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        ByUserAddress userAddress = BeanHelper.copyProperties(userAddressDTO, ByUserAddress.class);
        boolean b = userAddressService.updateById(userAddress);
        if (!b) {
            log.error("[by-user服务]updateUserAddress接口修改操作失败");
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 设置默认收货地址
     * put /address/setDefault/{id}
     *
     * @param addressId 地址id
     * @return 空
     */
    @ApiOperation("设置默认收货地址")
    @PutMapping(path = "/address/setDefault/{id}", name = "设置默认收货地址")
    public ResponseEntity<Void> setDefaultUserAddress(@PathVariable(name = "id") Long addressId) {
        log.info("[by-user服务]setDefaultUserAddress接口接收到请求,设置默认收货地址");
        userAddressService.setDefaultUserAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除地址信息
     * delete /address/{id}
     *
     * @param addressId 地址id
     * @return 空
     */
    @ApiOperation("删除地址信息")
    @DeleteMapping(path = "/address/{id}", name = "删除地址信息")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable(name = "id") Long addressId) {
        log.info("[by-user服务]deleteUserAddress接口接收到请求,删除地址信息");
        userAddressService.deleteUserAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
