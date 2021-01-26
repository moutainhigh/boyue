package com.boyue.cart.controller;

import com.boyue.cart.entity.Cart;
import com.boyue.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:20
 * @Author: Jacky
 * @Description: 购物车的controller，完成购物车的增删改查以及登录合并购物车
 */
@Slf4j
@RestController
public class CartController {

    /**
     * 注入购物车的service对象
     */
    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * 用户登陆后，点击添加购物车，把用户的购物车内容保存在服务端
     * 接口路径  POST  /
     *
     * @param cart 购物车数据
     * @return 空
     */
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        log.info("[cart服务]saveCart接口添加到购物车cart={}",cart);
        cartService.addCart(cart);
        return ResponseEntity.noContent().build();
    }

}
