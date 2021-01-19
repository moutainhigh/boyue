package com.boyue.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.item.entity.ByBrand;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByBrandMapper extends BaseMapper<ByBrand> {

}
