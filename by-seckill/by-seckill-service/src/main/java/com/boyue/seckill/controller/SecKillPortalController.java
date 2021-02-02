package com.boyue.seckill.controller;

import com.boyue.seckill.dto.OrderSecKillDTO;
import com.boyue.seckill.service.BySeckIllPortalService;
import com.boyue.task.client.ScheduleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:20
 * @Author: Jacky
 * @Description: 处理前端发送的秒杀请求的controller层
 */
@RestController
public class SecKillPortalController {

    @Autowired
    private ScheduleClient scheduleClient;
    @Autowired
    private BySeckIllPortalService seckIllPortalService;

    /**
     * 获取当前时间，返回给前端，做倒计时用
     * 格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return 时间对象的字符串
     */
    @GetMapping(path = "/portal/time",name = "获取当前时间")
    public ResponseEntity<String> getTime() {
        return ResponseEntity.ok(scheduleClient.getTime());
    }

    /**
     * 秒杀操作,创建秒杀订单
     *
     * @param orderSecKillDTO 秒杀的订单对象
     * @return 订单id
     */
    @PostMapping(path = "/portal/secKill",name = "秒杀操作")
    public ResponseEntity<Long> addSecKillOrder(@RequestBody OrderSecKillDTO orderSecKillDTO) {
        Long orderId = seckIllPortalService.addSecKillOrder(orderSecKillDTO);
        return ResponseEntity.ok(orderId);
    }
}

