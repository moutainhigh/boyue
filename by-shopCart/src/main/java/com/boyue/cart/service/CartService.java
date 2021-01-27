package com.boyue.cart.service;

import com.boyue.cart.entity.Cart;

import java.util.List;

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

    /**
     * 查询购物车
     * 通过请求，获取用户的购物车数据
     *
     * @return 查询出来的购物车数据的集合
     */
    List<Cart> findCarts();

    /**
     * 登录后合并购物车数据
     * 用户登录后，把客户端的购物车数据，传到服务端，批量增加/ 修改 购物车数据
     *
     * @param cartList 前台的购物车数据
     */
    void addCartBatch(List<Cart> cartList);

    /**
     * 删除用户的购物车的商品
     *
     * @param skuId 商品id
     */
    void deleteCart(Long skuId);

    /**
     * 修改用户的购物车的商品数量
     *
     * @param id  商品id
     * @param num 商品数量
     */
    void updateCart(Long id, Integer num);
}
