package com.boyue.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.threadlocals.UserHolder;
import com.boyue.common.utils.BeanHelper;
import com.boyue.user.dto.UserAddressDTO;
import com.boyue.user.entity.ByUserAddress;
import com.boyue.user.mapper.ByUserAddressMapper;
import com.boyue.user.service.ByUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
@Slf4j
public class ByUserAddressServiceImpl extends ServiceImpl<ByUserAddressMapper, ByUserAddress> implements ByUserAddressService {

    /**
     * 查询收货人地址信息
     *
     * @param id 地址id
     * @return 用户地址对象
     */
    @Override
    public UserAddressDTO findUserAddressById(Long id) {
        if (id == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //获取用户id
        Long userId = UserHolder.getUserId();

        //构造查询条件
        QueryWrapper<ByUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ByUserAddress::getId, id).eq(ByUserAddress::getUserId, userId);
        ByUserAddress userAddress = this.getOne(queryWrapper);
        if (userAddress == null) {
            throw new ByException(ExceptionEnum.USER_ADDRESS_NOT_FOUND);
        }

        return BeanHelper.copyProperties(userAddress, UserAddressDTO.class);
    }

    /**
     * 根据用户id获取用户地址信息
     *
     * @param userId 用户id
     * @param id     地址id
     * @return 用户地址对象
     */
    @Override
    public UserAddressDTO findUserAddressByUid(Long id, Long userId) {
        if (id == null || userId == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //构造查询条件
        QueryWrapper<ByUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ByUserAddress::getId, id).eq(ByUserAddress::getUserId, userId);
        ByUserAddress userAddress = this.getOne(queryWrapper);
        if (userAddress == null) {
            throw new ByException(ExceptionEnum.USER_ADDRESS_NOT_FOUND);
        }

        return BeanHelper.copyProperties(userAddress, UserAddressDTO.class);
    }

    /**
     * 查询用户的所有收货信息
     *
     * @return 用户地址对象的list集合
     */
    @Override
    public List<UserAddressDTO> findUserAddressList() {
        try {
            Long userId = UserHolder.getUserId();
            QueryWrapper<ByUserAddress> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ByUserAddress::getUserId, userId);
            List<ByUserAddress> userAddressList = this.list(queryWrapper);
            return BeanHelper.copyWithCollection(userAddressList, UserAddressDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ByException(ExceptionEnum.USER_ADDRESS_NOT_FOUND);
        }
    }

    /**
     * 设置默认收货地址
     *
     * @param addressId 地址id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultUserAddress(Long addressId) {
        QueryWrapper<ByUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ByUserAddress::getIsDefault, true);
        int count = this.count(queryWrapper);
        if (count > 0) {
            UpdateWrapper<ByUserAddress> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().
                    eq(ByUserAddress::getIsDefault, true).
                    set(ByUserAddress::getIsDefault, false);
            boolean flag = this.update(updateWrapper);
            if (!flag) {
                throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
            }
        }
        ByUserAddress userAddress = new ByUserAddress().setIsDefault(true).setId(addressId);
        boolean updateFlag = this.updateById(userAddress);
        if (!updateFlag) {
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
     * 删除地址信息
     *
     * @param addressId 地址id
     */
    @Override
    public void deleteUserAddress(Long addressId) {
        Long userId = UserHolder.getUserId();
        QueryWrapper<ByUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ByUserAddress::getUserId, userId).eq(ByUserAddress::getId, addressId);
        boolean flag = this.remove(queryWrapper);
        if (!flag) {
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * 根据 主键id 查询收货人地址信息
     * GET  /address/byId
     *
     * @param id 地址id
     * @return 地址信息
     */
    @Override
    public UserAddressDTO findAddressById(Long id) {
        //校验参数
        if (id == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //获取用户id
        Long userId = UserHolder.getUserId();
        //构造条件
        QueryWrapper<ByUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().
                eq(ByUserAddress::getId, id).
                eq(ByUserAddress::getUserId, userId);
        ByUserAddress userAddress = this.getOne(queryWrapper);
        if (userAddress == null) {
            throw new ByException(ExceptionEnum.USER_ADDRESS_NOT_FOUND);
        }
        return BeanHelper.copyProperties(userAddress, UserAddressDTO.class);
    }

    /**
     * 根据用户uid 和 收货人id 查询收货人信息
     * GET /address/byUser
     *
     * @param userId    用户id
     * @param addressId 收货人id
     * @return 用户地址userAddressDTO对象
     */
    @Override
    public UserAddressDTO findAddressByUser(Long userId, Long addressId) {
        //校验参数
        if (userId == null || addressId == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //构造条件
        QueryWrapper<ByUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().
                eq(ByUserAddress::getId, addressId).
                eq(ByUserAddress::getUserId, userId);
        ByUserAddress userAddress = this.getOne(queryWrapper);
        if (userAddress == null) {
            throw new ByException(ExceptionEnum.USER_ADDRESS_NOT_FOUND);
        }
        return BeanHelper.copyProperties(userAddress, UserAddressDTO.class);
    }
}
