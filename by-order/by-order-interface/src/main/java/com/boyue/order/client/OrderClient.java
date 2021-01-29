package com.boyue.order.client;

import com.boyue.vo.OrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 14:02
 * @Author: Jacky
 * @Description: order的client接口层
 */
@FeignClient(value = "order-service")
public interface OrderClient {
    /**
     * 根据订单id查询订单信息
     *
     * @param orderId 订单id
     * @return 订单的vo对象
     */
    @GetMapping(path = "/order/{id}",name = "根据订单id查询订单信息")
    OrderVo findOrderById(@PathVariable(name = "id") Long orderId);
}
