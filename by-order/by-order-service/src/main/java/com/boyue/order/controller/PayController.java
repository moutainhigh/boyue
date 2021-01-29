package com.boyue.order.controller;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.order.entity.ByOrder;
import com.boyue.order.service.ByOrderService;
import com.boyue.order.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 17:19
 * @Author: Jacky
 * @Description: 支付的controller
 */
@Api("订单支付的PayController")
@Slf4j
@RestController
public class PayController {
    /**
     * 注入订单的service
     */
    @Autowired
    private ByOrderService orderService;

    /**
     * 注入pay的service接口
     */
    @Autowired
    private PayService payService;

    /**
     * 调用微信支付统一下单接口
     * 获取codeUrl
     * 返回给前端生成二维码
     * 请求路径：GET  /order/url/{id}
     *
     * @param orderId 订单id
     * @return 订单codeUrl
     */
    @ApiOperation("调用微信支付统一下单接口")
    @GetMapping(path = "/order/url/{id}", name = "调用微信支付统一下单接口")
    public ResponseEntity<String> getCodeUrl(@PathVariable(name = "id") Long orderId) {
        log.info("[by-order服务]getCodeUrl接口接收到请求,调用微信支付统一下单接口");
        return ResponseEntity.ok(payService.getCodeUrl(orderId));
    }

    /**
     * 查询订单的状态
     * GET /order/state/{id}
     *
     * @param orderId 订单id
     * @return 订单状态
     */
    @ApiOperation("查询订单的状态")
    @GetMapping(path = "/order/state/{id}", name = "查询订单的状态")
    public ResponseEntity<Integer> getOrderStatus(@PathVariable(name = "id") Long orderId) {
        log.info("[by-order服务]getOrderStatus接口接收到请求,查询订单的状态");
        ByOrder order = orderService.getById(orderId);
        if (order == null) {
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        return ResponseEntity.ok(order.getStatus());
    }

    /**
     * 接收微信支付通知
     *
     * @param map 通知
     * @return 封装的响应码
     */
    @PostMapping(value = "/pay/wx/notify",produces = "application/xml")
    public Map<String, String> wxNotify(@RequestBody Map<String, String> map) {

        log.info("微信支付结果通知，map={}", map);
        //如果支付成功，修改订单的状态
        orderService.updateOrderStatus(map);
        //如果操作成功返回响应内容
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("return_code", "SUCCESS");
        resultMap.put("return_msg", "OK");
        return resultMap;
    }


}
