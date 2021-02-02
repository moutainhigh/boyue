package com.boyue.task.schedule;

import com.boyue.common.constants.RocketMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.boyue.common.constants.RocketMQConstants.TAGS.ORDER_OVERTIME_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TAGS.SECKILL_ORDER_OVERTIME_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.ORDER_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 20:28
 * @Author: Jacky
 * @Description:  订单超时的定时任务
 *  这里约定好任务的一些参数：
 *
 * - 任务执行频率：1分钟一次
 * - 订单超时未付款的期限：15分钟
 * - 任务锁的自动释放时间：30秒
 * - 任务锁等待时长：0
 */
@Slf4j
@Component
public class OrderOverTimeTask {
    private static final long FIX_DELAY_TIME = 60000L;
    private static final String LOCK_KEY = "by:order:close:schedule:lock";

    /**
     * 注入redisson对象
     */
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 注入rocketMQ对象
     */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * redisson自动释放锁时间
     */
    private final long TASK_LEASE_TIME = 30L;

    /**
     * 订单超时未付款的期限
     */
    private final int OVER_ORDER_MINUTES = 15;

    /**
     * 定时关闭业务
     * fixedRate  固定频率执行
     * fixedDelay 固定延迟执行
     */
    @Scheduled(fixedDelay = 60000L)
    public void closeOrder() {
        //构造任务锁对象
        RLock lock = redissonClient.getLock(LOCK_KEY);
        try {
            //获取分布式锁
            boolean b = lock.tryLock(0, TASK_LEASE_TIME, TimeUnit.SECONDS);
            if (!b) {
                // 获取锁失败，结束任务
                log.info("【清理订单任务】未能获取任务锁，结束任务。");
                return;
            }
            try {
                log.info("【清理订单任务】任务执行开始。");
                String dest = ORDER_TOPIC_NAME+":"+ ORDER_OVERTIME_TAGS;
                //计算本次查询的 过期时间点
                String overTime = DateTime.now().minusMinutes(15).toString("yyyy-MM-dd HH:mm:ss");
                log.info("发送的消息内容，{}",overTime);
                // 2.2清理订单
                rocketMQTemplate.convertAndSend(dest, overTime);
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            log.error("【清理订单任务】获取任务锁异常，原因：{}", e.getMessage(), e);
        } finally {
            // 任务结束，释放锁
            lock.unlock();
            log.info("【清理订单任务】任务执行完毕，释放锁。");
        }
    }

    /**
     * 每隔5分钟执行
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void closeSecKillOrder() {
        RLock lock = redissonClient.getLock("overTimeSeckillOrderTask");
        try {
            boolean b = lock.tryLock(0, 30, TimeUnit.SECONDS);
            if (!b) {
                // 获取锁失败，结束任务
                log.info("【清理秒杀订单任务】未能获取任务锁，结束任务。");
                return;
            }
            log.info("【清理秒杀订单任务】任务执行开始。");
            String desc = ORDER_TOPIC_NAME + ":" + SECKILL_ORDER_OVERTIME_TAGS;
            // 发送清理秒杀订单任务
            rocketMQTemplate.convertAndSend(desc, "清理秒杀订单");
        } catch (Exception e) {
            log.error("【清理秒杀订单任务】获取任务锁异常，原因：{}", e.getMessage(), e);
        } finally {
            // 任务结束，释放锁
            lock.unlock();
            log.info("【清理秒杀订单任务】任务执行完毕，释放锁。");
        }
    }
}
