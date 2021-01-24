package com.boyue.sms.listener;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.JsonUtils;
import com.boyue.common.utils.RegexUtils;
import com.boyue.sms.config.SmsProperties;
import com.boyue.sms.utils.SmsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static com.boyue.common.constants.RocketMQConstants.CONSUMER.SMS_VERIFY_CODE_CONSUMER;
import static com.boyue.common.constants.RocketMQConstants.TAGS.VERIFY_CODE_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.SMS_TOPIC_NAME;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 21:26
 * @Author: Jacky
 * @Description: 发送短信的监听器，接收参数是json字符串
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = SMS_TOPIC_NAME,
        selectorExpression = VERIFY_CODE_TAGS,
        consumerGroup = SMS_VERIFY_CODE_CONSUMER)
public class SmsListener implements RocketMQListener<String> {
    @Autowired
    private SmsHelper smsHelper;

    @Autowired
    private SmsProperties smsProperties;

    /**
     * 发送短信
     *
     * @param message 信息内容  {"phone":"number","code":"111"}
     */
    @Override
    public void onMessage(String message) {
        log.info("【by-sendSMS服务】接收到消息，内容={}", message);
        if (StringUtils.isBlank(message)) {
            log.error("[by-sendSMS服务]收到无效的请求参数");
        }
        //将message转换为map
        Map<String, String> map = JsonUtils.toMap(message, String.class, String.class);
        if (CollectionUtils.isEmpty(map)){
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
        String phone = map.remove("phone");
        if (StringUtils.isBlank(phone) || !RegexUtils.isPhone(phone)) {
            log.error("[by-sendSMS服务]电话号码错误！");
        }
        String code = JsonUtils.toString(map);

        smsHelper.sendMessage(phone, smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate(), code);
    }
}
