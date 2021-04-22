package com.boyue.item.service.impl;

import com.boyue.item.entity.ByCategoryBrand;
import com.boyue.item.mapper.ByCategoryBrandMapper;
import com.boyue.item.service.ByCategoryBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品分类和品牌的中间表，两者是多对多关系 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Service
public class ByCategoryBrandServiceImpl extends ServiceImpl<ByCategoryBrandMapper, ByCategoryBrand> implements ByCategoryBrandService {

}
