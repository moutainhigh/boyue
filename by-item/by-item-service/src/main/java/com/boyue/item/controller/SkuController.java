package com.boyue.item.controller;

import com.boyue.item.dto.SkuDTO;
import com.boyue.item.service.BySkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/20 20:55
 * @Author: Jacky
 * @Description: sku的controller接口层
 */
@RestController
@Slf4j
public class SkuController {

    /**
     * 注入skuService
     */
    @Autowired
    private BySkuService skuService;

    /**
     * 根据spu的id查询Sku集合接口
     * 请求路径  GET /sku/of/spu?id=1
     *
     * @param id 商品实体id
     * @return 商品实体
     */
    @GetMapping(path = "/sku/of/spu", name = "查询sku的商品实体")
    public ResponseEntity<List<SkuDTO>> findSkuBySpuId(@RequestParam(name = "id") Long id) {
        log.info("----- findSkuBySpuId接口，查询sku的商品实体 ------");
        List<SkuDTO> list = skuService.findSkuBySpuId(id);
        return ResponseEntity.ok(list);
    }
}
