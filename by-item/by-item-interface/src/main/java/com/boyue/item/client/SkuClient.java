package com.boyue.item.client;

import com.boyue.item.dto.SkuDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/20 20:55
 * @Author: Jacky
 * @Description: sku的client接口层
 */
@FeignClient(value = "item-service")
public interface SkuClient {
    /**
     * 根据spu的id查询Sku集合接口
     * 请求路径  GET /sku/of/spu?id=1
     *
     * @param id 商品实体id
     * @return 商品实体
     */
    @GetMapping(path = "/sku/of/spu", name = "查询sku的商品实体")
    List<SkuDTO> findSkuBySpuId(@RequestParam(name = "id") Long id);

    /**
     * 根据skuId的List集合查询sku集合
     *
     * @param ids skuId的集合
     * @return sku的集合
     */
    @GetMapping(path = "/sku/list", name = "根据skuId的List集合查询sku集合")
    List<SkuDTO> findSkuByListIds(@RequestParam(name = "ids") List<Long> ids);


    /**
     * 传递sku的ids，获取sku的集合数据
     * GET /sku/list?ids=27359021572,28359021572
     *
     * @param ids sku的id集合，如有多个用逗号分隔
     * @return skuDTO的集合
     */
    @GetMapping(path = "/sku/listIds", name = "传递sku的ids，获取sku的集合数据")
    List<SkuDTO> findSkuByIds(@RequestParam(name = "ids") String ids);
}
