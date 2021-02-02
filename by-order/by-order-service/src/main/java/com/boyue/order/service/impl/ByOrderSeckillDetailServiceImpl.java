package com.boyue.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.order.entity.ByOrderSeckillDetail;
import com.boyue.order.mapper.ByOrderSeckillDetailMapper;
import com.boyue.order.service.ByOrderSeckillDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 秒杀订单详情表 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class ByOrderSeckillDetailServiceImpl extends ServiceImpl<ByOrderSeckillDetailMapper, ByOrderSeckillDetail> implements ByOrderSeckillDetailService {

    /**
     * 查询秒杀的全部订单
     *
     * @return 秒杀的订单集合
     */
    @Override
    public List<ByOrderSeckillDetail> findOvertimeSeckillOrderDetail() {
        return this.baseMapper.findOvertimeSeckillOrderDetail();
    }
}
