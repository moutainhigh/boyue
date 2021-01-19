package com.boyue.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.item.entity.ByCategory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByCategoryMapper extends BaseMapper<ByCategory> {

    /**
     * 获取分类信息
     *
     * @param brandId 品牌id
     * @return 查询到的categoryDTO对象的集合
     */
    @Select("SELECT a.* FROM by_category a , by_category_brand  b WHERE a.id = b.category_id AND b.brand_id=#{brandId}")
    List<ByCategory> selectCategoryByBrandId(@Param("brandId") Long brandId);
}
