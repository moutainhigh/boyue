package com.boyue.page.listener;

import com.boyue.page.service.GoodsPageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.boyue.common.constants.RocketMQConstants.CONSUMER.ITEM_PAGE_UP_CONSUMER;
import static com.boyue.common.constants.RocketMQConstants.TAGS.ITEM_UP_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.ITEM_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 20:31
 * @Author: Jacky
 * @Description: 商品上架监听器，创建静态页面
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = ITEM_TOPIC_NAME,
        selectorExpression = ITEM_UP_TAGS,
        consumerGroup = ITEM_PAGE_UP_CONSUMER)
public class ItemPageUpListener implements RocketMQListener<Long> {

    /**
     * 注入商品页面的service
     */
    @Autowired
    private GoodsPageService goodsPageService;

    /**
     * 监听商品上架消息
     * 创建商品对应的静态页面
     * @param spuId 商品id
     */
    @Override
    public void onMessage(Long spuId) {
        log.info("[by-page服务]接收到商品上架消息，spuId={}",spuId);
        goodsPageService.createHtml(spuId);
    }
}
