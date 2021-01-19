package com.boyue.upload.service;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 15:59
 * @Author: Jacky
 * @Description:
 */
public interface FileUploadService {
    /**
     * 获取阿里云oss签名
     * @return 阿里云oss签名的json
     */
    Map<String,Object> signature();
}
