package com.boyue.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.user.dto.UserAddressDTO;
import com.boyue.user.entity.ByUserAddress;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByUserAddressService extends IService<ByUserAddress> {

    /**
     * 查询收货人地址信息
     *
     * @param id 地址id
     * @return 用户地址对象
     */
    UserAddressDTO findUserAddressById(Long id);

    /**
     * 根据用户id获取用户地址信息
     *
     * @param userId 用户id
     * @param id     地址id
     * @return 用户地址对象
     */
    UserAddressDTO findUserAddressByUid(Long id, Long userId);

    /**
     * 查询用户的所有收货信息
     *
     * @return 用户地址对象的list集合
     */
    List<UserAddressDTO> findUserAddressList();


    /**
     * 设置默认收货地址
     *
     * @param addressId 地址id
     */
    void setDefaultUserAddress(Long addressId);

    /**
     * 删除地址信息
     *
     * @param addressId 地址id
     */
    void deleteUserAddress(Long addressId);

    /**
     * 根据 主键id 查询收货人地址信息
     * GET  /address/byId
     * @param id 地址id
     * @return 地址信息
     */
    UserAddressDTO findAddressById(Long id);

    /**
     * 根据用户uid 和 收货人id 查询收货人信息
     * GET /address/byUser
     *
     * @param userId    用户id
     * @param addressId 收货人id
     * @return 用户地址userAddressDTO对象
     */
    UserAddressDTO findAddressByUser(Long userId, Long addressId);
}
