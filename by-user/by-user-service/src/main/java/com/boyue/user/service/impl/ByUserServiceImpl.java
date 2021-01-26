package com.boyue.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.utils.JsonUtils;
import com.boyue.common.utils.RegexUtils;
import com.boyue.user.dto.UserDTO;
import com.boyue.user.entity.ByUser;
import com.boyue.user.mapper.ByUserMapper;
import com.boyue.user.service.ByUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.boyue.common.constants.RocketMQConstants.TAGS.VERIFY_CODE_TAGS;
import static com.boyue.common.constants.RocketMQConstants.TOPIC.SMS_TOPIC_NAME;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
@Slf4j
public class ByUserServiceImpl extends ServiceImpl<ByUserMapper, ByUser> implements ByUserService {

    /**
     * redis中存储验证码的key
     */
    private static final String KEY_PREFIX = "by:user:code:phone:";

    /**
     * 注入springDataRedisTemplate，因为操作的是string类型数据，所以使用StringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 注入rocketMQTemplate
     */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * spring提供的密码加密策略
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 数据校验：
     * 实现用户数据的校验，主要包括对：手机号、用户名的唯一性校验。
     * 当用户注册时，会输入用户名或手机号，此时页面会异步请求服务端，
     * 服务端接收数据，校验数据的唯一性。
     *
     * @param data 要校验的数据
     * @param type 要校验的数据类型： 1.用户名；2.手机
     * @return true为可以用，false不可以用
     */
    @Override
    public Boolean checkData(String data, Integer type) {
        //构造查询对象
        QueryWrapper<ByUser> queryWrapper = new QueryWrapper<>();
        //设置查询条件
        switch (type) {
            case 1:
                log.info("[by-user服务]checkData处理校验数据类型是用户名");
                queryWrapper.lambda().eq(ByUser::getUsername, data);
                break;
            case 2:
                log.info("[by-user服务]checkData处理校验数据类型是手机号");
                queryWrapper.lambda().eq(ByUser::getPhone, data);
                break;
            default:
                log.error("[by-user服务]checkData处理校验数据类型不合法");
                throw new ByException(ExceptionEnum.INVALID_PARAMETER_TYPE_ERROR);
        }
        //查询结果集，统计个数
        int count = this.count(queryWrapper);
        log.info("[by-user服务]checkData处理校验数据统计的个数为{}",count);

        //结果集为0个则返回true，表示可用，否则返回false，表示不可用
        return count == 0;
    }

    /**
     * 发送短信验证码:
     * 用户输入手机号，点击发送验证码，前端会把手机号发送到服务端。
     * 服务端生成随机验证码，长度为6位，纯数字。并且调用短信服务，
     * 发送验证码到用户手机。
     * <p>
     * 步骤：
     * - 接收用户请求，手机号码
     * - 验证手机号格式
     * - 生成验证码
     * - 保存验证码到redis
     * - 发送RocketMQ消息到ly-sms
     *
     * @param phone 用户的手机号码
     */
    @Override
    public void sendSmsToPhone(String phone) {
        //校验参数是否争取，验证手机号格式
        if (StringUtils.isBlank(phone) || !RegexUtils.isPhone(phone) || !this.checkData(phone, 2)) {
            log.error("[by-user服务]sendSmsToPhone接收到的手机号不合法");
            throw new ByException(ExceptionEnum.UNAVAILABLE_PHONE_ERROR);
        }

        //查看redis中是否存储了该手机号的验证码数据
        //构建redis的key
        String codeKey = KEY_PREFIX + phone;
        String codeValue = redisTemplate.opsForValue().get(codeKey);
        //判断值是否为空
        if (StringUtils.isNotBlank(codeValue)){
            log.error("[by-user服务]sendSmsToPhone在redis中已经存在该手机号码的code={}",codeValue);
            throw new ByException(ExceptionEnum.SEND_MESSAGE_ERROR);
        }

        //不存在生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        //存放到redis中,key,value,时间，时间类型
        redisTemplate.opsForValue().set(codeKey,code,5, TimeUnit.MINUTES);
        log.info("[by-user服务]在redis中存入了该手机号发送的的code={}",code);

        //发送短信业务
        //构建rocketMQ的topic和tags
        String destination = SMS_TOPIC_NAME+":" + VERIFY_CODE_TAGS;
        //构造map集合
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        String payload = JsonUtils.toString(map);
        rocketMQTemplate.convertAndSend(destination, payload);
        log.info("[by-user服务]sendSmsToPhone验证码已发送");
    }

    /**
     * 用户注册：
     * 用户页面填写数据，发送表单到服务端，服务端实现用户注册功能。需要对用户密码进行加密存储，
     * 使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
     * 步骤：
     * - 验证短信验证码
     * - 生成言
     * - 密码加密
     * - 写入数据库
     *
     * @param user 注册用户对象
     * @param code   前端传过来的手机验证码
     */
    @Override
    public void register(ByUser user, String code) {
        //用户参数在接口已经校验过了
        //判断前台传递的验证码是否为NULL
        if (StringUtils.isBlank(code)){
            log.error("[by-user服务]register接收到的code为null");
            throw new ByException(ExceptionEnum.INVALID_CODE_ERROR);
        }
        //验证用户信息是否可以使用
        String username = user.getUsername();
        if (!this.checkData(username,1)){
            log.error("[by-user服务]register接收到的用户名不可用");
            throw new ByException(ExceptionEnum.UNAVAILABLE_USERNAME_ERROR);
        }
        //验证手机号码是否可以使用
        String phone = user.getPhone();
        if (!this.checkData(phone,2)){
            log.error("[by-user服务]register接收到的电话号不可用");
            throw new ByException(ExceptionEnum.UNAVAILABLE_PHONE_ERROR);
        }

        //校验验证码是否争取
        //构建key
        String key = KEY_PREFIX + phone;
        String codeValue = redisTemplate.opsForValue().get(key);
        if (!StringUtils.equals(code,codeValue)){
            log.error("[by-user服务]register接收code与redis中的code不同");
            throw new ByException(ExceptionEnum.INVALID_VERIFY_CODE);
        }

        //密码加密处理
        //使用spring的BCryptPasswordEncoder 使用随机盐进行加密处理
        String password = passwordEncoder.encode(user.getPassword());

        //将加密后的密码设置到user对象中
        user.setPassword(password);

        //存储到数据库
        boolean saveFlag = this.save(user);
        if (!saveFlag){
            log.error("[by-user服务]register注册失败");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        log.info("[by-user服务]register注册成功");
    }

    /**
     * 根据用户名和密码查询用户：
     * 查询功能，根据参数中的用户名和密码查询指定用户并且返回用户
     *
     * @param username 用户名
     * @param password 用户密码
     * @return userDTO对象
     */
    @Override
    public UserDTO findUserByUsernameAndPassword(String username, String password) {
        //构建查询对象
        QueryWrapper<ByUser> queryWrapper = new QueryWrapper<>();
        //设置查询条件
        queryWrapper.lambda().eq(ByUser::getUsername,username);
        //查询用户
        ByUser user = this.getOne(queryWrapper);

        //校验密码是否正确
        boolean eqFlag = passwordEncoder.matches(password, user.getPassword());
        if (!eqFlag){
            log.error("[by-user服务]findUserByUsernameAndPassword密码不正确");
            throw new ByException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

        return BeanHelper.copyProperties(user,UserDTO.class);
    }
}
