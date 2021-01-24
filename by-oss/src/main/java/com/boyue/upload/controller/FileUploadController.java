package com.boyue.upload.controller;

import com.boyue.upload.service.FileUploadService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 15:58
 * @Author: Jacky
 * @Description: 文件上传的controller层
 */
@RestController
@Api("文件管理中心FileUploadController")
@Slf4j
public class FileUploadController {

    /**
     * 注入fileUploadService
     */
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 获取阿里云oss签名
     * @return 阿里云oss签名
     */
    @ApiOperation(value = "获取阿里云oss签名")
    @GetMapping("/signature")
    public ResponseEntity<Map<String,Object>> signature(){
        log.info("[oss-service服务]signature接口接收到请求,正在完成签名");
        return ResponseEntity.ok(fileUploadService.signature());
    }
}
