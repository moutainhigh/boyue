package com.boyue.upload.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.upload.config.OSSProperties;
import com.boyue.upload.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 15:59
 * @Author: Jacky
 * @Description: 文件上传，签名阿里云oss
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    private OSS client;

    @Autowired
    private OSSProperties ossProperties;

    /**
     * 获取阿里云oss签名
     *
     * @return 阿里云oss签名
     */
    @Override
    public Map<String, Object> signature() {
        try {
            long expireTime = ossProperties.getExpireTime();
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, ossProperties.getMaxFileSize());
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, ossProperties.getDir());

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, Object> respMap = new LinkedHashMap<>();
            respMap.put("accessId", ossProperties.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", ossProperties.getDir());
            respMap.put("host", ossProperties.getHost());
            respMap.put("expire", expireEndTime);
            return respMap;
        } catch (Exception e) {
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }
}
