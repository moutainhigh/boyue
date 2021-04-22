package com.boyue.item.service.impl;

import com.boyue.item.entity.ByBrand;
import com.boyue.item.mapper.ByBrandMapper;
import com.boyue.item.service.ByBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Service
public class ByBrandServiceImpl extends ServiceImpl<ByBrandMapper, ByBrand> implements ByBrandService {

}
