package com.boyue.common.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 14:57
 * @Author: Jacky
 * @Description: 用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色（权限中会使用）
     */
    private String role;
}
