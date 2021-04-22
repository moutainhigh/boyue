package com.boyue.item.mapper;

import com.boyue.item.entity.ByCategoryBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品分类和品牌的中间表，两者是多对多关系 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
public interface ByCategoryBrandMapper extends BaseMapper<ByCategoryBrand> {

}
