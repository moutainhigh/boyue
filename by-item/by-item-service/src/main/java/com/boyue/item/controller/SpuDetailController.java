package com.boyue.item.controller;

import com.boyue.item.dto.SpuDetailDTO;
import com.boyue.item.service.BySpuDetailService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/20 20:52
 * @Author: Jacky
 * @Description: spuDetail的controller接口层
 */
@RestController
@Slf4j
@Api("商品服务中心SpuDetailController")
public class SpuDetailController {

    /**
     * 注入spuDetailService
     */
    @Autowired
    private BySpuDetailService spuDetailService;

    /**
     * 查询SpuDetail接口
     * 请求路径  GET /spu/detail?id=2
     *
     * @param id 商品id
     * @return SpuDetailDTO
     */
    @ApiOperation(value = "查询SpuDetail接口")
    @ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = "Long")
    @GetMapping(path = "/spu/detail", name = "查询SpuDetail接口")
    public ResponseEntity<SpuDetailDTO> findSpuDetailBySpuId(@RequestParam(name = "id") Long id) {
        log.info("----- findSpuDetailBySpuId接口，查询SpuDetail接口 ------");
        SpuDetailDTO spuDetailDTO = spuDetailService.findSpuDetailBySpuId(id);
        return ResponseEntity.ok(spuDetailDTO);
    }
}
