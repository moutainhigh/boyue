package com.boyue.item.service.impl;

import com.boyue.item.entity.ByApplication;
import com.boyue.item.mapper.ByApplicationMapper;
import com.boyue.item.service.ByApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务信息表，记录微服务的id，名称，密文，用来做服务认证 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Service
public class ByApplicationServiceImpl extends ServiceImpl<ByApplicationMapper, ByApplication> implements ByApplicationService {

}
