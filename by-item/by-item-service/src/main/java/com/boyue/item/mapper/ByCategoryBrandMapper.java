package com.boyue.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.item.entity.ByCategoryBrand;

/**
 * <p>
 * 商品分类和品牌的中间表，两者是多对多关系 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByCategoryBrandMapper extends BaseMapper<ByCategoryBrand> {

}
