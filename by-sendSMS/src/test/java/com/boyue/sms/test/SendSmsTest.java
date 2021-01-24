package com.boyue.sms.test;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static com.boyue.common.constants.RocketMQConstants.TAGS.VERIFY_CODE_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.SMS_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 21:31
 * @Author: Jacky
 * @Description: 短信服务单元测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SendSmsTest {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSendMessage() throws InterruptedException {
        Map<String,String> map = new HashMap<>();
        map.put("phone", "18745027903");
        map.put("code", "123456");
        rocketMQTemplate.convertAndSend(SMS_TOPIC_NAME +":"+ VERIFY_CODE_TAGS,map);
    }
}
