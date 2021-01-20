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
}
