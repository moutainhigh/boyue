package com.boyue.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.seckill.entity.BySeckillPolicy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 秒杀政策表 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-30
 */
public interface BySeckillPolicyMapper extends BaseMapper<BySeckillPolicy> {
    /**
     * 减库存
     * @param seckillId 秒杀id
     * @param num       数量
     * @return 执行结果
     */
    @Update("update by_seckill_policy set stock_count = stock_count - #{num} where id=#{id}")
    int minusStock(@Param("id") Long seckillId, @Param("num") Integer num);

    /**
     * 恢复秒杀库存
     * @param secKillId 秒杀商品id
     * @param num 数量
     * @return 执行结果
     */
    @Update("update by_seckill_policy set stock_count = stock_count + #{num} where id=#{id}")
    int plusStock(@Param("id")Long secKillId, @Param("num") Integer num);
}
