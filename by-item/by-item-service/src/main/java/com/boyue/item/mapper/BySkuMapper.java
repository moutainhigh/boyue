package com.boyue.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyue.item.entity.BySku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 Mapper 接口
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface BySkuMapper extends BaseMapper<BySku> {

    /**
     * 减库存
     *
     * @param skuId  商品id
     * @param number 商品数量
     * @return 保存结果
     */
    @Update("update by_sku set stock = stock - #{number} where id = #{skuId}")
    int minusStock(@Param("skuId") Long skuId, @Param("number") Integer number);

    /**
     * 加库存
     *
     * @param skuId 商品id
     * @param number 数量
     * @return 返回值
     */
    @Update("update by_sku set stock = stock + #{num} where id = #{skuId}")
    int plusStock(@Param("skuId") Long skuId, @Param("num") Integer number);
}
