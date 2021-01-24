package com.boyue.sms.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.JsonUtils;
import com.boyue.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.boyue.sms.constants.SmsConstants.*;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 21:13
 * @Author: Jacky
 * @Description: 短信服务的工具类
 */
@Component
@Slf4j
public class SmsHelper {
    private IAcsClient client;

    private SmsProperties prop;

    public SmsHelper(IAcsClient client, SmsProperties prop) {
        this.client = client;
        this.prop = prop;
    }

    /**
     * 发送消息的方法
     * @param phone 短信接收号码，手机号
     * @param signName 短信签名
     * @param templateCode 短信模板ID
     * @param templateParam 短信模板变量替换JSON串
     */
    public void sendMessage(String phone, String signName, String templateCode, String templateParam) {
        CommonRequest request = new CommonRequest();
        request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain(prop.getDomain());
        request.setVersion(prop.getVersion());
        request.setAction(prop.getAction());
        request.putQueryParameter(SMS_PARAM_KEY_PHONE, phone);
        request.putQueryParameter(SMS_PARAM_KEY_SIGN_NAME, signName);
        request.putQueryParameter(SMS_PARAM_KEY_TEMPLATE_CODE, templateCode);
        request.putQueryParameter(SMS_PARAM_KEY_TEMPLATE_PARAM, templateParam);

        try {
            CommonResponse response = client.getCommonResponse(request);
            if(response.getHttpStatus() >= 300){
                log.error("【sendSMS服务】发送短信失败。响应信息：{}", response.getData());
            }
            // 获取响应体
            Map<String, String> resp = JsonUtils.toMap(response.getData(), String.class, String.class);
            // 判断是否是成功
            if(!StringUtils.equals(OK, resp.get(SMS_RESPONSE_KEY_CODE))){
                // 不成功，
                log.error("【sendSMS服务】发送短信失败，原因{}", resp.get(SMS_RESPONSE_KEY_MESSAGE));
                throw new ByException(ExceptionEnum.SEND_MESSAGE_ERROR);
            }
            log.info("【sendSMS服务】发送短信成功，手机号：{}, 响应：{}", phone, response.getData());
        } catch (ServerException e) {
            log.error("【sendSMS服务】发送短信失败，服务端异常。", e);
        } catch (ClientException e) {
            log.error("【sendSMS服务】发送短信失败，客户端异常。", e);
        }
    }
}
