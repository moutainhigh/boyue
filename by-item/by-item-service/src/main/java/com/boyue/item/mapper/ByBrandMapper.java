package com.boyue.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.item.entity.ByBrand;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByBrandMapper extends BaseMapper<ByBrand> {

    /**
     * 根据cid查询品牌信息
     * @param cid
     * @return
     */
    @Select("SELECT a.* FROM by_brand a ,by_category_brand b WHERE b.brand_id = a.id AND b.category_id=#{cid}")
    List<ByBrand> findBrandByCategoryId(Long cid);
}
