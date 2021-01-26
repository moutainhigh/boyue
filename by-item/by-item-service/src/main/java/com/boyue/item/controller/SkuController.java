package com.boyue.item.controller;

import com.boyue.item.dto.SkuDTO;
import com.boyue.item.service.BySkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api("商品服务中心SkuController")
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
    @ApiOperation(value = "根据spu的id查询Sku集合接口")
    @ApiImplicitParam(name = "id", value = "商品实体id", required = true, dataType = "Long")
    @GetMapping(path = "/sku/of/spu", name = "查询sku的商品实体")
    public ResponseEntity<List<SkuDTO>> findSkuBySpuId(@RequestParam(name = "id") Long id) {
        log.info("----- findSkuBySpuId接口，查询sku的商品实体 ------");
        List<SkuDTO> list = skuService.findSkuBySpuId(id);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据skuId的List集合查询sku集合
     *
     * @param ids skuId的集合
     * @return sku的集合
     */
    @ApiOperation(value = "根据skuId的List集合查询sku集合")
    @GetMapping(path = "/sku/list", name = "根据skuId的List集合查询sku集合")
    public ResponseEntity<List<SkuDTO>> findSkuByListIds(@RequestParam(name = "ids") List<Long> ids) {
        log.info("----- findSkuByListIds接口，查询sku的商品实体 ------");
        return ResponseEntity.ok(skuService.findSkuByListIds(ids));
    }


    /**
     * 传递sku的ids，获取sku的集合数据
     * GET /sku/list?ids=27359021572,28359021572
     *
     * @param ids sku的id集合，如有多个用逗号分隔
     * @return skuDTO的集合
     */
    @ApiOperation(value = "传递sku的ids，获取sku的集合数据")
    @GetMapping(path = "/sku/listIds", name = "传递sku的ids，获取sku的集合数据")
    public ResponseEntity<List<SkuDTO>> findSkuByIds(@RequestParam(name = "ids") String ids) {
        log.info("----- findSkuByIds接口，传递sku的ids，获取sku的集合数据 ------");
        return ResponseEntity.ok(skuService.findSkuByIds(ids));
    }
}
