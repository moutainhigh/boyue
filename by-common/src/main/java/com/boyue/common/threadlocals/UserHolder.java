package com.boyue.common.threadlocals;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:07
 * @Author: Jacky
 * @Description: 用户信息的ThreadLocal的容器
 */
public class UserHolder {
    /**
     * 初始化一个threadLocal，用于存放登录用户的id
     */
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置userId
     *
     * @param userId 登录用户的id
     */
    public static void setUserId(Long userId) {
        threadLocal.set(userId);
    }

    /**
     * 获取用户的id
     *
     * @return 用户的userId
     */
    public static Long getUserId() {
        return threadLocal.get();
    }

    /**
     * 删除用户的id
     */
    public static void removeUserId() {
        threadLocal.remove();
    }
}
