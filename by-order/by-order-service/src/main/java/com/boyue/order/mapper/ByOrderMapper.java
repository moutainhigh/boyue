package com.boyue.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.order.entity.ByOrder;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByOrderMapper extends BaseMapper<ByOrder> {

    /**
     * 查询超时没有支付的订单号
     * @param time 时间
     * @return 订单的id集合
     */
    @Select("select order_id from by_order where create_time < #{time} and status = 1")
    List<Long> selectOverTimeOrderId(String time);
}
