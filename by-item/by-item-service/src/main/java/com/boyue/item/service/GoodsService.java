package com.boyue.item.service;

import com.boyue.item.dto.SpuDTO;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/20 20:05
 * @Author: Jacky
 * @Description: 统一管理商品的service接口层
 */
public interface GoodsService {
    /**
     * 新增商品信息
     *
     * @param spuDTO 商品对象
     */
    void saveGoods(SpuDTO spuDTO);

    /**
     * 修改商品上下架，更新spu信息，同时需要更新sku
     *
     * @param id       商品id
     * @param saleable 上下架状态
     */
    void updateSaleable(Long id, Boolean saleable);

    /**
     * 修改商品
     *
     * @param spuDTO 商品对象
     */
    void updateGoods(SpuDTO spuDTO);

    /**
     * 根据id删除商品spu数据
     *
     * @param id 品牌id
     */
    void deleteSpuById(Long id);

    /**
     * 减库存
     *
     * @param map 商品id和数量键值对
     */
    void minusStock(Map<Long, Integer> map);

    /**
     * 加库存
     *
     * @param map 商品id和数量键值对
     */
    void plusStock(Map<Long, Integer> map);
}
