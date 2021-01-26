package com.boyue.common.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 14:52
 * @Author: Jacky
 * @Description: 封装载荷数据
 */
@Data
public class Payload<T> {
    /**
     * jwt的唯一标识id
     */
    private String id;

    /**
     * 用户数据
     */
    private T userInfo;

    /**
     * 过期时间
     */
    private Date expiration;
}
