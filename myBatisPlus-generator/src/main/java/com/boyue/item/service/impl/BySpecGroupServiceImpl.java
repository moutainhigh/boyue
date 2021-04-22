package com.boyue.item.service.impl;

import com.boyue.item.entity.BySpecGroup;
import com.boyue.item.mapper.BySpecGroupMapper;
import com.boyue.item.service.BySpecGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 规格参数的分组表，每个商品分类下有多个规格参数组 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Service
public class BySpecGroupServiceImpl extends ServiceImpl<BySpecGroupMapper, BySpecGroup> implements BySpecGroupService {

}
