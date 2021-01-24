package com.boyue.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 21:12
 * @Author: Jacky
 * @Description: 将SmsProperties注册到spring中
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfiguration {
    @Bean
    public IAcsClient acsClient(SmsProperties prop){
        DefaultProfile profile = DefaultProfile.getProfile(
                prop.getRegionID(), prop.getAccessKeyID(), prop.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }
}
