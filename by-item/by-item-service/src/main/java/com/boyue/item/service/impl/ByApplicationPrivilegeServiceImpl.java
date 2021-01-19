package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.item.entity.ByApplicationPrivilege;
import com.boyue.item.mapper.ByApplicationPrivilegeMapper;
import com.boyue.item.service.ByApplicationPrivilegeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务中间表，记录服务id以及服务能访问的目标服务的id 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class ByApplicationPrivilegeServiceImpl extends ServiceImpl<ByApplicationPrivilegeMapper, ByApplicationPrivilege> implements ByApplicationPrivilegeService {

}
