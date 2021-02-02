package com.boyue.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/28 23:51
 * @Author: Jacky
 * @Description:
 */
@Slf4j
@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String path = template.path();
        if(!path.startsWith("/address/byId")){
            return ;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取前端传递到服务端的request
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //获取头信息
        Enumeration<String> headerNames = request.getHeaderNames();
        //遍历头信息
        while (headerNames.hasMoreElements()){
            String headName = headerNames.nextElement();
            if ("cookie".equals(headName)){
                //判断是否cookie内容
                String cookieValue = request.getHeader(headName);
                //放入feign的请求头中
                template.header(headName,cookieValue);
                break;
            }
        }
    }
}
