package com.boyue.seckill.service;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.utils.JsonUtils;
import com.boyue.seckill.dto.SeckillPolicyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:25
 * @Author: Jacky
 * @Description: 秒杀的 redis 操作 service接口
 */
public interface BySecKillRedisService {
    /**
     * 把秒杀商品数据和 队列生成
     * 秒杀数据使用 string类型
     * 队列使用
     */
    void putSeckillPolicyToRedis(SeckillPolicyDTO seckillPolicyDTO);

    /**
     * 删除秒杀数据
     */
    void removeSeckill(Long secKillId);

    /**
     * 验证秒杀的时间
     *
     * @param seckillId
     */
    void isValiteTime(Long seckillId);

    /**
     * 判断秒杀是否成功
     *
     * @param seckillId
     */
    boolean hasSeckillGoods(Long seckillId);

    /**
     * 恢复库存
     * @param seckillIdAndNumMap 参数
     */
    void plusStock(Map<Long, Integer> seckillIdAndNumMap);
}
