package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.item.entity.ByManagerUser;
import com.boyue.item.mapper.ByManagerUserMapper;
import com.boyue.item.service.ByManagerUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class ByManagerUserServiceImpl extends ServiceImpl<ByManagerUserMapper, ByManagerUser> implements ByManagerUserService {

}
