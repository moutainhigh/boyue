package com.boyue.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.dto.OrderDTO;
import com.boyue.order.entity.ByOrder;
import com.boyue.vo.OrderVo;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByOrderService extends IService<ByOrder> {

    /**
     * 用户登陆后，提交购物车数据，创建订单
     *
     * @param orderDTO 订单数据
     * @return 订单id
     */
    Long creatOrder(OrderDTO orderDTO);

    /**
     * 根据订单id查询订单信息
     *
     * @param orderId 订单id
     * @return 订单的vo对象
     */
    OrderVo findOrderById(Long orderId);

    /**
     * 查询订单信息
     *
     * @param orderId 订单id
     * @param userId  用户id
     * @param status  订单状态
     * @return 查询出的订单对象
     */
    ByOrder findOrder(Long orderId, Long userId, Integer status);

    /**
     * 修改订单状态
     *
     * @param map 响应的商品信息
     */
    void updateOrderStatus(Map<String, String> map);

    /**
     * 关闭 超时未支付的订单
     *
     * @param time 当前时间-15分钟
     */
    void closeOverTimeOrder(String time);
}
