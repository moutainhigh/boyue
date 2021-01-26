package com.boyue.cart.service;

import com.boyue.cart.entity.Cart;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:21
 * @Author: Jacky
 * @Description: 购物车service层，处理购物车的业务逻辑
 */
public interface CartService {
    /**
     * 添加购物车
     * 用户登陆后，点击添加购物车，把用户的购物车内容保存在服务端
     *
     * @param cart 购物车数据
     */
    void addCart(Cart cart);
}
