package com.boyue.seckill.service;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.threadlocals.UserHolder;
import com.boyue.seckill.dto.OrderSecKillDTO;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:25
 * @Author: Jacky
 * @Description: 处理前端发送的秒杀请求的service层
 */
public interface BySeckIllPortalService {
    /**
     * 秒杀操作,创建秒杀订单
     *
     * @param orderSecKillDTO 秒杀的订单对象
     * @return 订单id
     */
    Long addSecKillOrder(OrderSecKillDTO orderSecKillDTO);
}
