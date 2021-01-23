package com.boyue.item.client;

import com.boyue.item.dto.SkuDTO;
import org.springframework.cloud.openfeign.FeignClient;
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
}
