package com.boyue.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 20:30
 * @Author: Jacky
 * @Description: 定时任务的controller接口
 */
@RestController
@Slf4j
public class TimeController {
    /**
     * 获取当前时间
     *
     * @return 时间的字符串
     */
    @GetMapping(path = "/getTime",name = "获取当前时间")
    public ResponseEntity<String> getTime() {

        return ResponseEntity.ok(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取当前日期
     *
     * @return 当前时间的字符串
     */
    @GetMapping(path = "/getToday",name = "获取当前日期")
    public ResponseEntity<String> getToday() {

        return ResponseEntity.ok(DateTime.now().toString("yyyy-MM-dd"));
    }

    /**
     * 获取昨天日期
     *
     * @return 昨天的字符串时间
     */
    @GetMapping(path = "/getYesterday",name = "获取昨天日期")
    ResponseEntity<String> getYesterday() {
        return ResponseEntity.ok(DateTime.now().minusDays(1).toString("yyyy-MM-dd"));
    }
}
