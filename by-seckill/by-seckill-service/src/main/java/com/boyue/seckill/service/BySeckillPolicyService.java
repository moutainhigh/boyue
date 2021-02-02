package com.boyue.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.seckill.entity.BySeckillPolicy;

import java.util.Map;

/**
 * <p>
 * 秒杀政策表 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-30
 */
public interface BySeckillPolicyService extends IService<BySeckillPolicy> {
    /**
     * 减库存
     * @param seckillId 秒杀id
     * @param num 数量
     */
    int minusStock(Long seckillId, Integer num);

    /**
     * 恢复mysql的库存
     * @param seckillIdAndNumMap
     */
    void plusStock(Map<Long, Integer> seckillIdAndNumMap);
}
