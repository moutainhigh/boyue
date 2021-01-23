package com.boyue.search.listener;

import com.boyue.search.service.SearchGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.boyue.common.constants.RocketMQConstants.CONSUMER.ITEM_PAGE_DOWN_CONSUMER;
import static com.boyue.common.constants.RocketMQConstants.TAGS.ITEM_DOWN_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.ITEM_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 20:25
 * @Author: Jacky
 * @Description: 商品下架监听器，删除商品再es中的索引
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = ITEM_TOPIC_NAME,
        selectorExpression = ITEM_DOWN_TAGS,
        consumerGroup = ITEM_PAGE_DOWN_CONSUMER)
public class ItemSearchDownListener implements RocketMQListener<Long> {
    /**
     * 注入searchGoodsService
     */
    @Autowired
    private SearchGoodsService searchGoodsService;

    /**
     * 商品下架消息
     * 删除索引
     * @param spuId  商品id
     */
    @Override
    public void onMessage(Long spuId) {
        log.info("[by-search服务]接收到商品下架消息，spuId={}",spuId);
        searchGoodsService.removeIndex(spuId);
    }
}
