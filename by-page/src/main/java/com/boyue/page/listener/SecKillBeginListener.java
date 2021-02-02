package com.boyue.page.listener;

import com.boyue.page.service.GoodsPageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.boyue.common.constants.RocketMQConstants.CONSUMER.SECKILL_BEGIN_CONSUMER;
import static com.boyue.common.constants.RocketMQConstants.TAGS.SECKILL_BEGIN_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.SECKILL_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 19:03
 * @Author: Jacky
 * @Description: 创建秒杀页面的消息监听器
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = SECKILL_BEGIN_CONSUMER,
        topic = SECKILL_TOPIC_NAME,
        selectorExpression = SECKILL_BEGIN_TAGS)
public class SecKillBeginListener implements RocketMQListener<String> {
    @Autowired
    private GoodsPageService goodsPageService;

    /**
     * 接收 创建静态页的消息
     * @param date 秒杀的日期
     */
    @Override
    public void onMessage(String date) {
        log.info("接收到创建秒杀页面消息，date={}",date);
        goodsPageService.createSecKillPage(date);
    }
}
