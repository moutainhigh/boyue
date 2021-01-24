package com.boyue.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserDTO  {
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 注册手机号
     */
    private String phone;
}
