package com.boyue.item.client;

import com.boyue.item.dto.SpuDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/20 20:52
 * @Author: Jacky
 * @Description: spuDetail的controller接口层
 */
@FeignClient(value = "item-service")
public interface SpuDetailClient {

    /**
     * 查询SpuDetail接口
     * 请求路径  GET /spu/detail?id=2
     *
     * @param id 商品id
     * @return SpuDetailDTO
     */
    @GetMapping(path = "/spu/detail", name = "查询SpuDetail接口")
    SpuDetailDTO findSpuDetailBySpuId(@RequestParam(name = "id") Long id);
}
