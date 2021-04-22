package com.boyue.item.mapper;

import com.boyue.item.entity.ByBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
public interface ByBrandMapper extends BaseMapper<ByBrand> {

}
