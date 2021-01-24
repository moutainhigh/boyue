package com.boyue.item.controller;

import com.boyue.item.dto.SpuDTO;
import com.boyue.item.service.GoodsServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/20 17:06
 * @Author: Jacky
 * @Description: 商品的controller层
 */
@RestController
@Slf4j
@Api("商品服务中心GoodsController")
public class GoodsController {

    private final GoodsServer goodsServer;

    @Autowired
    public GoodsController(GoodsServer goodsServer) {
        this.goodsServer = goodsServer;
    }

    /**
     * 新增商品信息
     * 请求路径  POST /goods
     *
     * @param spuDTO 商品对象
     * @return 空
     */
    @ApiOperation(value = "新增商品信息")
    @PostMapping(path = "/goods", name = "新增商品信息")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDTO spuDTO) {
        log.info("-----   saveGoods接口，完成新增商品信息   -------");
        goodsServer.saveGoods(spuDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改商品上下架，更新spu信息，同时需要更新sku
     * 请求路径   PUT /spu/saleable?id=1&saleable=1
     *
     * @param id       商品id
     * @param saleable 上下架状态
     * @return 空
     */
    @ApiOperation(value = "修改商品上下架，更新spu信息，同时需要更新sku")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "商品id",dataType = "Long"),
            @ApiImplicitParam(name = "saleable",value = "上下架状态",dataType = "Boolean")
    })
    @PutMapping(path = "/spu/saleable", name = "修改商品上下架，更新spu信息，同时需要更新sku")
    public ResponseEntity<Void> updateSaleable(@RequestParam(name = "id") Long id,
                                               @RequestParam(name = "saleable") Boolean saleable) {
        log.info("----- updateSaleable接口，修改商品上下架 ------");
        goodsServer.updateSaleable(id, saleable);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改商品
     * 请求路径  PUT /goods
     *
     * @param spuDTO 商品对象
     * @return 空
     */
    @ApiOperation(value = "修改商品")
    @PutMapping(path = "/goods", name = "修改商品")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuDTO spuDTO) {
        log.info("----- updateGoods接口，修改商品 ------");
        goodsServer.updateGoods(spuDTO);

        return ResponseEntity.noContent().build();
    }

    /**
     * 根据id删除商品spu数据
     * DELETE /delete/spu/{id}
     *
     * @param id 品牌id
     * @return 空
     */
    @ApiOperation(value = "根据id删除商品spu数据")
    @ApiImplicitParam(name = "id", value = "品牌id", required = true, dataType = "Long")
    @DeleteMapping(path = "/delete/spu/{id}", name = "根据id删除商品spu数据")
    public ResponseEntity<Void> deleteSpuById(@PathVariable(value = "id") Long id) {
        log.info("----- deleteSpuById接口，根据id删除商品spu数据  ------");
        goodsServer.deleteSpuById(id);
        return ResponseEntity.noContent().build();
    }
}
