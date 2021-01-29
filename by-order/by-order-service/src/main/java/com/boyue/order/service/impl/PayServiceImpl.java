package com.boyue.order.service.impl;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.order.entity.ByOrder;
import com.boyue.order.service.ByOrderService;
import com.boyue.order.service.PayService;
import com.boyue.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 17:29
 * @Author: Jacky
 * @Description: 支付的service接口
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    /**
     * 注入微信支付的工具类
     */
    @Autowired
    private PayHelper payHelper;

    /**
     * 注入orderService对象
     */
    @Autowired
    private ByOrderService orderService;

    /**
     * 注入redis的template对象
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * reidsKey的前缀
     */
    String PRE_FIX = "by:pay:orderId:";
    /**
     * 调用微信支付统一下单接口
     * 获取codeUrl
     * 返回给前端生成二维码
     * 请求路径：GET  /order/url/{id}
     *
     * @param orderId 订单id
     * @return 订单codeUrl
     */
    @Override
    public String getCodeUrl(Long orderId) {
        //校验参数
        if (orderId == null){
            log.error("无效的请求参数");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //构建redisKey
        String key = PRE_FIX + orderId;
        String codeUrl = redisTemplate.opsForValue().get(key);
        log.info("redis中缓存的codeUrl = {}",codeUrl);
        //如果redis中存在则直接返回
        if(StringUtils.isNotBlank(codeUrl)){
            return codeUrl;
        }
        String desc = "铂悦商城支付";
        //根据orderId查询订单信息
        ByOrder order = orderService.getById(orderId);
        if(order == null){
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        //获取订单的实付金额,用于测试都改成1分钱
        //tbOrder.getActualFee();
        //TODO 测试将支付金额改为1分钱
        Long actualFee = 1L;
        //调用统一下单接口，获取codeurl，有效期2小时
        codeUrl = payHelper.createCodeUrl(orderId, actualFee, desc);
        log.info("获取的codeurl is {}",codeUrl);
        redisTemplate.opsForValue().set(key,codeUrl,2, TimeUnit.HOURS);
        return codeUrl;
    }
}
