package com.boyue.order.listener;

import com.boyue.order.service.ByOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.boyue.common.constants.RocketMQConstants.CONSUMER.ORDER_OVERTIME_CONSUMER;
import static com.boyue.common.constants.RocketMQConstants.TAGS.ORDER_OVERTIME_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.ORDER_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 20:10
 * @Author: Jacky
 * @Description: 订单业务 清理超时订单 监听
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = ORDER_TOPIC_NAME,
        selectorExpression = ORDER_OVERTIME_TAGS,
        consumerGroup = ORDER_OVERTIME_CONSUMER)
public class OverTimeOrderListener implements RocketMQListener<String> {

    /**
     * 注入订单的service
     */
    @Autowired
    private ByOrderService orderService;

    /**
     * 监听器执行方法
     * 订单业务 清理超时订单 监听
     * @param overDate 超时时间
     */
    @Override
    public void onMessage(String overDate) {
        log.info("接收到 overtime 消息 : 内容：{}", overDate);
        orderService.closeOverTimeOrder(overDate);
    }
}
