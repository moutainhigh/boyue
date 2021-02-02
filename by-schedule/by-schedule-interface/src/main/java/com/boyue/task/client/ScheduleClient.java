package com.boyue.task.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 19:38
 * @Author: Jacky
 * @Description: schedule-service服务的feignClient接口
 */
@FeignClient("schedule-service")
public interface ScheduleClient {

    /**
     * 获取当前时间
     *
     * @return 时间的字符串
     */
    @GetMapping(path = "/getTime",name = "获取当前时间")
    String getTime();

    /**
     * 获取当前日期
     *
     * @return 当前时间的字符串
     */
    @GetMapping(path = "/getToday",name = "获取当前日期")
    String getToday();

    /**
     * 获取昨天日期
     *
     * @return 昨天的字符串时间
     */
    @GetMapping(path = "/getYesterday",name = "获取昨天日期")
    String getYesterday();
}
