package com.boyue.cart.controller;

import com.boyue.cart.entity.Cart;
import com.boyue.cart.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:20
 * @Author: Jacky
 * @Description: 购物车的controller，完成购物车的增删改查以及登录合并购物车
 */
@Api("购物车服务CartController")
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
    @ApiOperation(value = "添加购物车")
    @PostMapping(name = "添加购物车")
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        log.info("[cart服务]saveCart接口添加到购物车cart={}",cart);
        cartService.addCart(cart);
        return ResponseEntity.noContent().build();
    }

    /**
     * 查询购物车
     * 通过请求，获取用户的购物车数据
     * 接口路径  GET /list
     *
     * @return 查询出来的购物车数据的集合
     */
    @ApiOperation(value = "查询购物车")
    @GetMapping(path = "/list", name = "查询购物车")
    public ResponseEntity<List<Cart>> findCarts() {
        log.info("[cart服务]saveCart接口查询购物车");
        List<Cart> list = cartService.findCarts();
        return ResponseEntity.ok(list);
    }

    /**
     * 登录后合并购物车数据
     * 用户登录后，把客户端的购物车数据，传到服务端，批量增加/ 修改 购物车数据
     * 接口路径POST /list
     *
     * @param cartList 前台的购物车数据
     * @return 空
     */
    @ApiOperation(value = "登录后合并购物车数据")
    @PostMapping(path = "/list", name = "登录后合并购物车数据")
    public ResponseEntity<Void> addCartBatch(@RequestBody List<Cart> cartList) {
        log.info("[cart服务]addCartBatch接口登录后合并购物车数据");
        cartService.addCartBatch(cartList);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除用户的购物车的商品
     * 接口路径DELETE /3355143
     *
     * @param skuId 商品id
     * @return 空
     */
    @ApiOperation(value = "删除用户的购物车的商品")
    @DeleteMapping(path = "/{id}", name = "删除用户的购物车的商品")
    public ResponseEntity<Void> deleteCart(@PathVariable(name = "id") Long skuId) {
        log.info("[cart服务]deleteCart接口删除用户的购物车的商品");
        cartService.deleteCart(skuId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改用户的购物车的商品数量
     * 接口路径PUT /
     *
     * @param id  商品id
     * @param num 商品数量
     * @return 空
     */
    @ApiOperation(value = "修改用户的购物车的商品数量")
    @PutMapping(path = "", name = "修改用户的购物车的商品数量")
    public ResponseEntity<Void> updateCart(@RequestParam(name = "id") Long id,
                                           @RequestParam(name = "num") Integer num) {
        log.info("[cart服务]updateCart接口修改用户的购物车的商品数量");
        cartService.updateCart(id, num);
        return ResponseEntity.noContent().build();
    }
}
