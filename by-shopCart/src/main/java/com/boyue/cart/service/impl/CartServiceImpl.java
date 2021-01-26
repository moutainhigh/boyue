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
}
