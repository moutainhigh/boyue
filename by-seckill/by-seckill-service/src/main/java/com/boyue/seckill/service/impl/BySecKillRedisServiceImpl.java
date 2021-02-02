package com.boyue.seckill.service.impl;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.JsonUtils;
import com.boyue.seckill.dto.SeckillPolicyDTO;
import com.boyue.seckill.service.BySecKillRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:22
 * @Author: Jacky
 * @Description: 秒杀的 redis 操作 service接口实现类
 */
@Slf4j
@Service
public class BySecKillRedisServiceImpl implements BySecKillRedisService {

    /**
     * 存储秒杀商品 + seckillId
     */
    private final String SEC_KILL_GOODS_PREFIX = "by:seckill:goods:";

    /**
     * 存储秒杀商品库存队列
     */
    private final String SEC_KILL_GOODS_NUM_PREFIX = "by:seckill:goods:num:";

    /**
     * 注入redis的template
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 把秒杀商品数据和 队列生成
     * 秒杀数据使用 string类型
     * 队列使用
     *
     * @param seckillPolicyDTO 秒杀商品对象
     */
    @Override
    public void putSeckillPolicyToRedis(SeckillPolicyDTO seckillPolicyDTO) {
        Long secKillId = seckillPolicyDTO.getId();
        String seckillKey = SEC_KILL_GOODS_PREFIX + secKillId;
        //设置商品信息
        redisTemplate.opsForValue().set(seckillKey, JsonUtils.toString(seckillPolicyDTO));
        //设置队列信息
        String listKey = SEC_KILL_GOODS_NUM_PREFIX + secKillId;
        //获取队列的商品的库存数量
        Integer stockCount = seckillPolicyDTO.getStockCount();
        for (int i = 0; i < stockCount; i++) {
            redisTemplate.opsForList().leftPush(listKey, secKillId.toString());
        }
    }

    /**
     * 删除秒杀数据
     *
     * @param secKillId 秒杀商品id
     */
    @Override
    public void removeSeckill(Long secKillId) {
        String seckillKey = SEC_KILL_GOODS_PREFIX + secKillId;
        redisTemplate.delete(seckillKey);
        //设置队列信息
        String listKey = SEC_KILL_GOODS_NUM_PREFIX + secKillId;
        redisTemplate.delete(listKey);
    }

    /**
     * 验证秒杀的时间
     *
     * @param seckillId 秒杀商品id
     */
    @Override
    public void isValiteTime(Long seckillId) {

        //秒杀信息的key
        String key = SEC_KILL_GOODS_PREFIX + seckillId;
        //获取秒杀信息
        String json = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(json)) {
            throw new ByException(ExceptionEnum.SECKILL_NOT_FOUND);
        }
        SeckillPolicyDTO seckillPolicyDTO = JsonUtils.toBean(json, SeckillPolicyDTO.class);
        //获取开始时间
        long beginTime = seckillPolicyDTO.getBeginTime().getTime();
        //获取结束时间
        long endTime = seckillPolicyDTO.getEndTime().getTime();
        //获取当前时间
        long time = System.currentTimeMillis();
        if (time < beginTime) {
            throw new ByException(ExceptionEnum.SECKILL_NOT_BEGIN);
        }
        if (time > endTime) {
            throw new ByException(ExceptionEnum.SECKILL_IS_END);
        }
    }

    /**
     * 判断秒杀是否成功
     *
     * @param seckillId
     */
    @Override
    public boolean hasSeckillGoods(Long seckillId) {
        String listKey = SEC_KILL_GOODS_NUM_PREFIX + seckillId;
        //从队列中获取元素
        String str = redisTemplate.opsForList().leftPop(listKey);
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return true;
    }

    /**
     * 恢复库存
     *
     * @param seckillIdAndNumMap 参数
     */
    @Override
    public void plusStock(Map<Long, Integer> seckillIdAndNumMap) {
        for (Long id : seckillIdAndNumMap.keySet()) {
            Integer num = seckillIdAndNumMap.get(id);
            for (int i = 0; i < num; i++) {
                //有多少库存就压栈多少次
                redisTemplate.boundListOps(SEC_KILL_GOODS_NUM_PREFIX + id).leftPush(id.toString());
            }
        }
    }
}
