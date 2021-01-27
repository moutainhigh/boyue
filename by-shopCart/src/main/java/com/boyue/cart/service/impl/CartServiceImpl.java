package com.boyue.cart.service.impl;

import com.boyue.cart.entity.Cart;
import com.boyue.cart.service.CartService;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.threadlocals.UserHolder;
import com.boyue.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:22
 * @Author: Jacky
 * @Description: 购物车service层，处理购物车的业务逻辑
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {
    /**
     * 注入
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * redisKey的前缀
     */
    private String PRE_FIX = "by:cart:uid:";

    /**
     * 添加购物车
     * 用户登陆后，点击添加购物车，把用户的购物车内容保存在服务端
     *
     * @param cart 购物车数据
     */
    @Override
    public void addCart(Cart cart) {
        //校验参数是否有效
        if (cart == null){
            log.error("【cart服务】addCart接口接收到的购物参数为空");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        try {
            //获取用户的userId
            Long userId = UserHolder.getUserId();
            log.info("【cart服务】addCart接口用户的userId={}",userId);

            //校验参数是否有效
            if (userId == null){
                log.error("【cart服务】addCart接口用户id为空");
                throw new ByException(ExceptionEnum.USER_ID_NOT_FOUND);
            }

            //构建redisKey
            String redisKey = PRE_FIX + userId;

            //构建hashKey
            String hashKey = cart.getSkuId().toString();

            //构建boundHashOps.存储购物车数据
            BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(redisKey);

            //通过hashOps获取redis中的json数据
            String cartSkuJson = hashOps.get(hashKey);

            //判断参数是否为空
            if (StringUtils.isNotBlank(cartSkuJson)){
                //不为空，需要与购物车中的数据进行累加
                //购物车中商品的个数
                Integer cartNum = cart.getNum();
                //数据类型转换
                cart = JsonUtils.toBean(cartSkuJson, Cart.class);
                assert cart != null;
                cart.setNum(cartNum + cart.getNum());
            }
            //将处理后的cart存储到redis中
            hashOps.put(hashKey, JsonUtils.toString(cart));
        } catch (ByException e) {
            e.printStackTrace();
            throw new ByException(ExceptionEnum.UNAUTHORIZED);
        }
    }

    /**
     * 查询购物车
     * 通过请求，获取用户的购物车数据
     *
     * @return 查询出来的购物车数据的集合
     */
    @Override
    public List<Cart> findCarts() {
        List<Cart> list = null;
        try {
            //获取threadLocal中的用户id
            Long userId = UserHolder.getUserId();
            //构建redisKey
            String redisKey = PRE_FIX + userId;
            //构建  boundHashOps
            BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(redisKey);
            //获取所有的数据
            List<String> cartJson = hashOps.values();
            if (CollectionUtils.isEmpty(cartJson)) {
                throw new ByException(ExceptionEnum.CARTS_NOT_FOUND);
            }

            //收集cart的list集合
            list = cartJson.stream().map(cart -> {
                return JsonUtils.toBean(cart, Cart.class);
            }).collect(Collectors.toList());
        } catch (ByException e) {
            e.printStackTrace();
            throw new ByException(ExceptionEnum.CARTS_NOT_FOUND);
        }

        return list;
    }

    /**
     * 登录后合并购物车数据
     * 用户登录后，把客户端的购物车数据，传到服务端，批量增加/ 修改 购物车数据
     *
     * @param cartList 前台的购物车数据
     */
    @Override
    public void addCartBatch(List<Cart> cartList) {
        //校验参数
        if (CollectionUtils.isEmpty(cartList)){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //通过threadLocal获取userId
        Long userId = UserHolder.getUserId();
        //构建redisKey
        String redisKey = PRE_FIX + userId;
        //构建boundHashOps
        BoundHashOperations<String, String, String> boundHashOps = redisTemplate.boundHashOps(redisKey);
        //循环cartList
        for (Cart cart : cartList) {
            //构建hashKey
            String hashKey = cart.getSkuId().toString();
            //查找商品
            String cartJson = boundHashOps.get(hashKey);
            Integer cartNum = cart.getNum();
            if (StringUtils.isNotBlank(cartJson)) {
                cart = JsonUtils.toBean(cartJson, Cart.class);
                assert cart != null;
                cart.setNum(cartNum + cart.getNum());
            }
            //存入到redis中
            boundHashOps.put(hashKey, Objects.requireNonNull(JsonUtils.toString(cart)));
        }
    }

    /**
     * 删除用户的购物车的商品
     *
     * @param skuId 商品id
     */
    @Override
    public void deleteCart(Long skuId) {
        //校验参数
        if (skuId == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        try {
            //通过threadLocal获取userId
            Long userId = UserHolder.getUserId();
            //构建redisKey
            String redisKey = PRE_FIX + userId;
            //构建hashKey
            String hashKey = skuId.toString();
            //删除购物车中的商品
            redisTemplate.opsForHash().delete(redisKey, hashKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * 修改用户的购物车的商品数量
     *
     * @param id  商品id
     * @param num 商品数量
     */
    @Override
    public void updateCart(Long id, Integer num) {
        //校验参数
        if (id == null || num == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        try {
            //通过threadLocal获取登录用户的userId
            Long userId = UserHolder.getUserId();
            //构建redisKey
            String redisKey = PRE_FIX + userId;
            //构建hashKey
            String hashKey = id.toString();
            //构建boundHashOps
            BoundHashOperations<String, String, String> boundHashOps = redisTemplate.boundHashOps(redisKey);
            //获取cartJson
            String cartJson = boundHashOps.get(hashKey);
            if (StringUtils.isBlank(cartJson)){
                log.error("购物车商品不存在，用户：{}, 商品：{}", userId, id);
                throw new ByException(ExceptionEnum.CARTS_NOT_FOUND);
            }
            //转换为cart对象
            Cart cart = JsonUtils.toBean(cartJson, Cart.class);
            //设置商品数量
            assert cart != null;
            cart.setNum(num);
            //存入到redis中
            boundHashOps.put(hashKey,JsonUtils.toString(cart));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }
}
