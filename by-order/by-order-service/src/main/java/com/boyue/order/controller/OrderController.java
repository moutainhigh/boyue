package com.boyue.order.controller;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.dto.OrderDTO;
import com.boyue.order.entity.ByOrder;
import com.boyue.order.service.ByOrderService;
import com.boyue.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/28 21:19
 * @Author: Jacky
 * @Description: 订单服务的controller
 */
@Api("订单服务的OrderController")
@RestController
@Slf4j
public class OrderController {
    @Autowired
    private ByOrderService orderService;

    /**
     * 用户登陆后，提交购物车数据，创建订单
     * 接口路径 POST /order
     *
     * @param orderDTO 订单数据
     * @return 订单id
     */
    @ApiOperation("创建订单")
    @PostMapping(path = "/order", name = "用户登陆后，提交购物车数据，创建订单")
    public ResponseEntity<Long> creatOrder(@RequestBody OrderDTO orderDTO) {
        log.info("[by-order服务]creatOrder接口接收到请求,创建订单");
        return ResponseEntity.ok(orderService.creatOrder(orderDTO));
    }

    /**
     * 根据订单id查询订单信息
     *
     * @param orderId 订单id
     * @return 订单的vo对象
     */
    @ApiOperation("根据订单id查询订单信息")
    @GetMapping(path = "/order/{id}",name = "根据订单id查询订单信息")
    public ResponseEntity<OrderVo> findOrderById(@PathVariable(name = "id") Long orderId) {
        log.info("[by-order服务]findOrderById接口接收到请求,创建订单");
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    /**
     * 查询秒杀订单
     *
     * @param orderId 订单id
     * @return 订单的vo对象
     */
    @GetMapping("/order/findOrder")
    public ResponseEntity<OrderVo> findOrder(@RequestParam(name = "id",required = false) Long orderId) {
        return ResponseEntity.ok(orderService.findOrderByOrderId(orderId));
    }
}
