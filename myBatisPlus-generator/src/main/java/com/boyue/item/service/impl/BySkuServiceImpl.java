package com.boyue.item.service.impl;

import com.boyue.item.entity.BySku;
import com.boyue.item.mapper.BySkuMapper;
import com.boyue.item.service.BySkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Service
public class BySkuServiceImpl extends ServiceImpl<BySkuMapper, BySku> implements BySkuService {

}
