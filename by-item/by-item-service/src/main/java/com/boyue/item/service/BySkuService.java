package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.item.dto.SkuDTO;
import com.boyue.item.entity.BySku;

import java.util.List;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface BySkuService extends IService<BySku> {

    /**
     * 根据spu的id查询Sku集合接口
     *
     * @param id 商品实体id
     * @return 商品实体
     */
    List<SkuDTO> findSkuBySpuId(Long id);

    /**
     * 根据skuId的List集合查询sku集合
     *
     * @param ids skuId的集合
     * @return sku的集合
     */
    List<SkuDTO> findSkuByListIds(List<Long> ids);

    /**
     * 传递sku的ids，获取sku的集合数据
     *
     * @param ids sku的id集合，如有多个用逗号分隔
     * @return skuDTO的集合
     */
    List<SkuDTO> findSkuByIds(String ids);

    /**
     * 减库存
     *
     * @param skuId  商品id
     * @param number 商品数量
     * @return 保存结果
     */
    int minusStock(Long skuId, Integer number);

    /**
     * 加库存
     *
     * @param skuId 商品id
     * @param number 数量
     * @return 返回值
     */
    int plusStock(Long skuId, Integer number);
}
