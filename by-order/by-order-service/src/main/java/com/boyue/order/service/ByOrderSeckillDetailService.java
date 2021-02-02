package com.boyue.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.order.entity.ByOrderSeckillDetail;

import java.util.List;

/**
 * <p>
 * 秒杀订单详情表 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByOrderSeckillDetailService extends IService<ByOrderSeckillDetail> {

    /**
     * 查询秒杀的全部订单
     *
     * @return 秒杀的订单集合
     */
    List<ByOrderSeckillDetail> findOvertimeSeckillOrderDetail();
}
