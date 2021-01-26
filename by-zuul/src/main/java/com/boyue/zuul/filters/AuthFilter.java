package com.boyue.zuul.filters;

import com.boyue.common.auth.entity.Payload;
import com.boyue.common.auth.entity.UserInfo;
import com.boyue.common.auth.utils.JwtUtils;
import com.boyue.common.utils.CookieUtils;
import com.boyue.zuul.config.FilterProperties;
import com.boyue.zuul.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 20:20
 * @Author: Jacky
 * @Description: 用户权限的拦截器
 */
@Slf4j
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties properties;

    @Autowired
    private FilterProperties filterProperties;

    /**
     * 拦截器类型
     * 前置类型
     *
     * @return 拦截器类型
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 当前过滤器的执行顺序
     *
     * @return 数字越小，优先级越高
     */
    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    /**
     * 是否需要过滤
     *
     * @return true则执行过滤器，false不执行
     */
    @Override
    public boolean shouldFilter() {
        return !isAllowPath();
    }

    /**
     * 判断是否 不登录就可以访问
     * @return true - 可以不登录  false -必须登录
     */
    private boolean isAllowPath() {
        List<String> allowPaths = filterProperties.getAllowPaths();
        //获取到上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        log.info("请求路径：{}",path);
        for (String allowPath : allowPaths) {
            if (path.startsWith(allowPath)){
                return true;
            }
        }

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //获取到上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = ctx.getRequest();
        try {
            //获取token
            String token = CookieUtils.getCookieValue(request, properties.getUser().getCookieName());
            //解析token
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, properties.getPublicKey(), UserInfo.class);

            //获取用户对象
            UserInfo userInfo = payload.getUserInfo();
            Long id = userInfo.getId();
            String username = userInfo.getUsername();
            String role = userInfo.getRole();
            //获取当前资源路径
            String path = request.getRequestURI();
            String method = request.getMethod();
            // TODO 判断权限，此处暂时空置，等待权限服务完成后补充
            log.info("【网关】用户{},角色{}。访问服务{} : {}，", userInfo.getUsername(), role, method, path);
        } catch (Exception e) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            log.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e);
        }
        return null;
    }
}
