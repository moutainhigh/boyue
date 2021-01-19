package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.item.entity.BySpu;
import com.boyue.item.mapper.BySpuMapper;
import com.boyue.item.service.BySpuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class BySpuServiceImpl extends ServiceImpl<BySpuMapper, BySpu> implements BySpuService {

}
