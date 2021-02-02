package com.boyue.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.order.entity.ByOrderSeckillDetail;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 秒杀订单详情表 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByOrderSeckillDetailMapper extends BaseMapper<ByOrderSeckillDetail> {

    /**
     * 查询秒杀的全部订单
     *
     * @return 秒杀的订单集合
     */
    @Select("SELECT osd.seckill_id, sum(osd.num) num FROM by_order o, by_order_seckill_detail osd WHERE " +
            " o.order_id = osd.order_id  AND b_type = 2  AND o. STATUS = 1 AND TIMESTAMPDIFF(MINUTE, o.create_time, NOW()) > 5 " +
            "GROUP BY osd.seckill_id")
    List<ByOrderSeckillDetail> findOvertimeSeckillOrderDetail();
}
