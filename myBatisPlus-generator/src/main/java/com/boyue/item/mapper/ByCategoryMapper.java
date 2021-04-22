package com.boyue.item.mapper;

import com.boyue.item.entity.ByCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
public interface ByCategoryMapper extends BaseMapper<ByCategory> {

}
