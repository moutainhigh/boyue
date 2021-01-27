package com.boyue.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户收货地址表
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserAddressDTO  {


    /**
     * 地址id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人名称
     */
    private String addressee;

    /**
     * 收货人电话
     */
    private String phone;

    /**
     * 收货人省份
     */
    private String province;

    /**
     * 收货人市
     */
    private String city;

    /**
     * 收货人区
     */
    private String district;

    /**
     * 收货人详细地址
     */
    private String address;

    /**
     * 收货人邮编
     */
    private String postcode;

    /**
     * 是否默认 0-不是  1-是
     */
    private Boolean isDefault;
}
