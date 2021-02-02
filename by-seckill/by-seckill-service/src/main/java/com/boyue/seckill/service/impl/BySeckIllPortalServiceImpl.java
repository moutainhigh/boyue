package com.boyue.seckill.service.impl;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.threadlocals.UserHolder;
import com.boyue.common.utils.IdWorker;
import com.boyue.seckill.dto.OrderSecKillDTO;
import com.boyue.seckill.service.BySecKillRedisService;
import com.boyue.seckill.service.BySeckIllPortalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.boyue.common.constants.RocketMQConstants.TAGS.SECKILL_ORDER_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.ORDER_TOPIC_NAME;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.SECKILL_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:22
 * @Author: Jacky
 * @Description: 处理前端发送的秒杀请求的service层实现类
 */
@Service
@Slf4j
public class BySeckIllPortalServiceImpl implements BySeckIllPortalService {

    /**
     * 秒杀的 redis 操作 service接口
     */
    @Autowired
    private BySecKillRedisService secKillRedisService;

    /**
     * 注入雪花算法工具类
     */
    @Autowired
    private IdWorker idWorker;

    /**
     * 注入rocketMQ的template对象
     */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 秒杀操作,创建秒杀订单
     *
     * @param orderSecKillDTO 秒杀的订单对象
     * @return 订单id
     */
    @Override
    public Long addSecKillOrder(OrderSecKillDTO orderSecKillDTO) {
        //获取用户id
        Long userId = UserHolder.getUserId();
        //获取秒杀id
        Long seckillId = orderSecKillDTO.getSeckillId();
        //验证秒杀信息，判断时间
        secKillRedisService.isValiteTime(seckillId);
        //到redis的队列中获取数据,判断用户是否秒杀成功
        boolean b = secKillRedisService.hasSeckillGoods(seckillId);
        if (!b) {
            throw new ByException(ExceptionEnum.SECKILL_IS_END);
        }
        //用户已经抢购到商品，创建订单
        //使用雪花算法生成订单id
        long orderId = idWorker.nextId();

        //把订单id和用户id 放入接收的对象中，作为消息内容
        orderSecKillDTO.setOrderId(orderId);
        orderSecKillDTO.setUserId(userId);

        //把订单信息用消息中间件发送给order服务
        String dest = ORDER_TOPIC_NAME + ":" + SECKILL_ORDER_TAGS;
        rocketMQTemplate.convertAndSend(dest, orderSecKillDTO);
        return orderId;
    }
}
