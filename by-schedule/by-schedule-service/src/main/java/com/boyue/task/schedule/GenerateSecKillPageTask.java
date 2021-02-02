package com.boyue.task.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.boyue.common.constants.RocketMQConstants.TAGS.SECKILL_BEGIN_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.SECKILL_TOPIC_NAME;


/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 18:56
 * @Author: Jacky
 * @Description: 秒杀 业务 定时任务
 */
@Slf4j
@Component
public class GenerateSecKillPageTask {
    /**
     * 注入rocketMQ的template
     */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 秒杀页面生成的消息定时任务
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void doSeckillTask() {
        String dest = SECKILL_TOPIC_NAME + ":" + SECKILL_BEGIN_TAGS;
        String payload = DateTime.now().toString("yyyy-MM-dd");
        log.info("秒杀定时任务开始，dest={},payload={}", dest, payload);
        rocketMQTemplate.convertAndSend(dest, payload);
    }
}
