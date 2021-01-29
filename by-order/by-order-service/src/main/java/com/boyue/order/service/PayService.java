package com.boyue.order.service;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 17:29
 * @Author: Jacky
 * @Description: 支付的service接口
 */
public interface PayService {
    /**
     * 调用微信支付统一下单接口
     * 获取codeUrl
     * 返回给前端生成二维码
     * 请求路径：GET  /order/url/{id}
     *
     * @param orderId 订单id
     * @return 订单codeUrl
     */
    String getCodeUrl(Long orderId);
}
