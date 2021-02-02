package com.boyue.order.listener;

import com.boyue.order.service.ByOrderService;
import com.boyue.seckill.dto.OrderSecKillDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.boyue.common.constants.RocketMQConstants.CONSUMER.SECKILL_ORDER_CONSUMER;
import static com.boyue.common.constants.RocketMQConstants.TAGS.SECKILL_ORDER_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.ORDER_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 20:32
 * @Author: Jacky
 * @Description:
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = SECKILL_ORDER_CONSUMER,
        topic = ORDER_TOPIC_NAME,
        selectorExpression = SECKILL_ORDER_TAGS)
public class SecKillOrderListener implements RocketMQListener<OrderSecKillDTO> {

    /**
     * 注入order的service接口
     */
    @Autowired
    private ByOrderService orderService;

    /**
     * 接收秒杀订单创建消息
     *
     * @param orderSecKillDTO 秒杀订单的dto对象
     */
    @Override
    public void onMessage(OrderSecKillDTO orderSecKillDTO) {
        log.info("接收到秒杀订单消息，内容={}", orderSecKillDTO);
        orderService.createSecKillOrder(orderSecKillDTO);
    }
}
