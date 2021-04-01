package com.boyue.order.controller;

import com.boyue.common.vo.PageResult;
import com.boyue.dto.OrderDTO;
import com.boyue.dto.OrderStatisticsDTO;
import com.boyue.order.service.ByOrderService;
import com.boyue.order.service.ByOrderStatisticsService;
import com.boyue.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    /**
     * 注入订单的service
     */
    @Autowired
    private ByOrderService orderService;

    /**
     * 注入订单统计的service
     */
    @Autowired
    private ByOrderStatisticsService orderStatisticsService;

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
    @GetMapping(path = "/order/{id}", name = "根据订单id查询订单信息")
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
    public ResponseEntity<OrderVo> findOrder(@RequestParam(name = "id", required = false) Long orderId) {
        return ResponseEntity.ok(orderService.findOrderByOrderId(orderId));
    }


    /**
     * 查询订单信息
     * http://api.boyue.com/api/order/list?page=1&rows=5
     *
     * @param page 当前页
     * @param rows 每页显示行数
     * @return 查询到的订单的list集合
     */
    @ApiOperation("查询订单信息")
    @GetMapping(path = "/order/list", name = "查询订单信息")
    public ResponseEntity<PageResult<OrderVo>> findOrderList(@RequestParam(name = "page", required = false) Long page,
                                                             @RequestParam(name = "rows", required = false) Long rows) {
        log.info("[by-order服务]findOrderList接口接收到请求,查询订单信息");
        return ResponseEntity.ok(orderService.findOrderList(page, rows));
    }

    /**
     * 功能说明：获取订单每月的销售情况
     * 接口：/order/statistics?year=2021
     *
     * @param page   当前页码
     * @param rows   每页显示条数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @return 返回当年每月的销售订单的数量和销售总额
     */
    @ApiOperation(value = "获取订单每月的销售情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "当前年份/搜索年份", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "page", value = "当前页码", required = false, dataType = "Integer", defaultValue = "1"),
            @ApiImplicitParam(name = "rows", value = "每页显示条数", required = false, dataType = "Integer", defaultValue = "10"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段", required = false, dataType = "String"),
            @ApiImplicitParam(name = "desc", value = "是否降序", required = false, dataType = "Boolean", defaultValue = "false")
    })
    @GetMapping(path = "/order/statistics", name = "获取订单每月的销售情况")
    public ResponseEntity<PageResult<OrderStatisticsDTO>> statistics(@RequestParam(name = "year", required = false,defaultValue = "2021") Integer year,
                                                                     @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                     @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
                                                                     @RequestParam(name = "sortBy", required = false,defaultValue = "desc") String sortBy,
                                                                     @RequestParam(name = "desc", required = false, defaultValue = "false") Boolean desc) {
        log.info("调用statistics接口");
        PageResult<OrderStatisticsDTO> pageResult = orderStatisticsService.statistics(year, page, rows, sortBy, desc);
        return ResponseEntity.ok(pageResult);
    }
}
