package com.boyue.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/31 16:08
 * @Author: Jacky
 * @Description: 后台登录的用户DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDTO {
    private String username;
    private String password;
}
