package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.item.entity.ByUser;
import com.boyue.item.mapper.ByUserMapper;
import com.boyue.item.service.ByUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class ByUserServiceImpl extends ServiceImpl<ByUserMapper, ByUser> implements ByUserService {

}
