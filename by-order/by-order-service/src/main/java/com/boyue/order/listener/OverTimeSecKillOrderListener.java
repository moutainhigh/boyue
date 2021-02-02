package com.boyue.order.listener;

import com.boyue.common.constants.RocketMQConstants;
import com.boyue.order.service.ByOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.boyue.common.constants.RocketMQConstants.CONSUMER.SECKILL_ORDER_OVERTIME_CONSUMER;
import static com.boyue.common.constants.RocketMQConstants.TAGS.SECKILL_ORDER_OVERTIME_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.ORDER_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 21:23
 * @Author: Jacky
 * @Description: 关闭秒杀订单的listener
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = ORDER_TOPIC_NAME,
        selectorExpression = SECKILL_ORDER_OVERTIME_TAGS,
        consumerGroup = SECKILL_ORDER_OVERTIME_CONSUMER)
public class OverTimeSecKillOrderListener implements RocketMQListener<String> {

    /**
     * 注入订单的orderService
     */
    @Autowired
    private ByOrderService orderService;

    /**
     * 闭秒杀订单的listener
     *
     * @param message 消息通知
     */
    @Override
    public void onMessage(String message) {
        log.info("接收到 秒杀  overtime 消息 ");
        orderService.closeOverTimeSecKillOrder();
    }
}
