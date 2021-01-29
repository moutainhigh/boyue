package com.boyue.item.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/28 22:21
 * @Author: Jacky
 * @Description: goods的client接口层
 */
@FeignClient(value = "item-service")
public interface GoodsClient {
    /**
     * 减库存
     *
     * @param map 商品id和数量键值对
     */
    @PutMapping(path = "/sku/minusStock", name = "减库存")
    void minusStock(@RequestBody Map<Long, Integer> map);

    /**
     * 加库存
     *
     * @param map 商品id和数量键值对
     * @return 空
     */
    @PutMapping("/sku/plusStock")
    Void plusStock(@RequestBody Map<Long, Integer> map);
}
