package com.boyue.auth.service.impl;

import com.boyue.auth.config.JwtProperties;
import com.boyue.auth.service.AuthService;
import com.boyue.common.auth.entity.Payload;
import com.boyue.common.auth.entity.UserInfo;
import com.boyue.common.auth.utils.JwtUtils;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.CookieUtils;
import com.boyue.user.client.UserClient;
import com.boyue.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/25 18:46
 * @Author: Jacky
 * @Description: 授权中心的service接口
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    /**
     * redis中存储过期token的键
     */
    private static final String KEY_PREFIX = "by:user:jwt:id:";

    /**
     * 注入user的feignClient接口
     */
    @Autowired
    private UserClient userClient;

    /**
     * 注入jwt的配置对象
     */
    @Autowired
    private JwtProperties properties;

    /**
     * 注入redisTemplate
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 用户从前端登录页面，输入用户名、密码，进行登录。
     *
     * @param username 用户名
     * @param password 用户密码
     * @param response 响应
     */
    @Override
    public void login(String username, String password, HttpServletResponse response) {
        //校验参数
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        try {
            //远程调用user查询用户对象
            UserDTO user = userClient.findUserByUsernameAndPassword(username, password);

            //构建userInfo
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setRole("admin");
            //通过jwt工具类生成token
            String token = JwtUtils.generateTokenExpireInMinutes(userInfo, properties.getPrivateKey(), properties.getUser().getExpire());

            //将生成的token存储到cookie中
            CookieUtils.newCookieBuilder()
                    //存入token
                    .value(token)
                    //token字cookie中存储的名称
                    .name(properties.getUser().getCookieName())
                    //设置域名
                    .domain(properties.getUser().getCookieDomain())
                    //设置为only，保证安全，防止xss攻击，不允许js操作cookie
                    .httpOnly(true)
                    //写入cookie中
                    .response(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("[auth-service服务]login接口出现错误，登录失败");
            throw new ByException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }

    /**
     * 用户从前端登录成功后，前端携带cookie包含token，到服务端验证token有效性，并返回用户信息
     *
     * @param request  请求  用来获取token
     * @param response 响应
     * @return 用户信息
     */
    @Override
    public UserInfo verify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取token
            String token = CookieUtils.getCookieValue(request, properties.getUser().getCookieName());
            //判断token是否有效
            if (StringUtils.isBlank(token)){
                log.error("[auth-service服务]verify接口：用户未登录或登录失效");
                throw new ByException(ExceptionEnum.UNAUTHORIZED);
            }
            log.info("token的值为token={}",token);

            //解析token
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, properties.getPublicKey(), UserInfo.class);
            //获取id
            String jwtId = payload.getId();
            //构建jwtKey
            String jwtKey = KEY_PREFIX + jwtId;

            //判断该token是否在黑名单中
            Boolean flag = redisTemplate.hasKey(jwtKey);
            if (flag != null && flag){
                log.error("[auth-service服务]verify接口：用户未登录或登录失效");
                throw new ByException(ExceptionEnum.UNAUTHORIZED);
            }

            //获取userInfo用户信息
            UserInfo userInfo = payload.getUserInfo();
            //检测userInfo是否为空
            if (userInfo == null) {
                log.error("[auth-service服务]verify接口：userInfo为null");
                throw new ByException(ExceptionEnum.UNAUTHORIZED);
            }

            //刷新token，完成token的续约
            //获取当前token的过期时间
            Date expiration = payload.getExpiration();

            //计算最早的刷新点时间
            DateTime flushTime = new DateTime(expiration).minusMillis(properties.getUser().getMinRefreshInterval());
            log.info("flushTime={}",flushTime);
            //判断当前时间是否大于最早刷新时间
            if (flushTime.isBeforeNow()){
                //如果大于则重新生成token，存入到cookie中
                String newToken = JwtUtils.generateTokenExpireInMinutes(userInfo, properties.getPrivateKey(), properties.getUser().getExpire());

                //将新生成的token存储到cookie中
                CookieUtils.newCookieBuilder()
                        //存入token
                        .value(newToken)
                        //token字cookie中存储的名称
                        .name(properties.getUser().getCookieName())
                        //设置域名
                        .domain(properties.getUser().getCookieDomain())
                        //设置为only，保证安全，防止xss攻击，不允许js操作cookie
                        .httpOnly(true)
                        //写入cookie中
                        .response(response).build();
            }
            return userInfo;
        } catch (ByException e) {
            log.info("获取用户数据失败");
            e.printStackTrace();
            throw new ByException(ExceptionEnum.UNAUTHORIZED);
        }
    }

    /**
     * 用户退出操作
     *
     * @param request  请求  用来获取token
     * @param response 响应
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取当前token
            String token = CookieUtils.getCookieValue(request, properties.getUser().getCookieName());
            //校验token是否有效
            if (StringUtils.isBlank(token)){
                log.error("[auth-service服务]logout接口：用户未登录或登录失效");
                throw new ByException(ExceptionEnum.UNAUTHORIZED);
            }
            //解析token
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, properties.getPublicKey(), UserInfo.class);
            //获取tokenId
            String jwtId = payload.getId();
            //获取过期时间
            Date expiration = payload.getExpiration();
            //计算剩余有效时间
            long effectiveTime = expiration.getTime() - System.currentTimeMillis();
            //将无效token存入redis
            //构造jwtKey
            String jwtKey = KEY_PREFIX + jwtId;
            redisTemplate.opsForValue().set(jwtKey,"Invalid Token",effectiveTime, TimeUnit.MICROSECONDS);
        } catch (ByException e) {
            log.info("退出操作失败");
            e.printStackTrace();
        } finally {
            //删除cookie中的token
            CookieUtils.deleteCookie(
                    properties.getUser().getCookieName(),
                    properties.getUser().getCookieDomain(),
                    response);
        }
    }
}
