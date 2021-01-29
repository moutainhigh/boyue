package com.boyue.item.controller;

import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.SpuDTO;
import com.boyue.item.service.BySpuService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/19 20:11
 * @Author: Jacky
 * @Description: spu的controller接口层
 */
@RestController
@Slf4j
@Api("商品服务中心SpuController")
public class SpuController {

    @Autowired
    private BySpuService spuService;

    /**
     * 查询商品SPU信息 ，分页查询
     * GET /spu/page
     *
     * @param page     当前页
     * @param rows     每页显示条数
     * @param key      过滤条件
     * @param saleable 上架或下架
     * @return pageResult对象
     */
    @ApiOperation(value = "查询商品SPU信息 ，分页查询")
    @GetMapping(path = "/spu/page", name = "查询商品SPU信息 ，分页查询")
    public ResponseEntity<PageResult<SpuDTO>> findAllOfSpu(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                           @RequestParam(name = "rows", required = false, defaultValue = "5") Integer rows,
                                                           @RequestParam(name = "key", required = false) String key,
                                                           @RequestParam(name = "saleable", required = false) Boolean saleable) {
        log.info("查询商品SPU信息  findAllOfSpu");
        PageResult<SpuDTO> spuDTO = spuService.findAllOfSpu(page, rows, key, saleable);
        return ResponseEntity.ok(spuDTO);
    }

    /**
     * 根据主键id查询sou信息
     *
     * @param id 主键id
     * @return spuDTO对象
     */
    @ApiOperation(value = "根据主键id查询sou信息")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping(path = "/spu/{id}", name = "根据主键id查询spu信息")
    public ResponseEntity<SpuDTO> findSpuById(@PathVariable(name = "id") Long id) {
        log.info("----- findSpuById接口，根据主键id查询sou信息 ------");
        SpuDTO spuDTO = spuService.findSpuById(id);
        return ResponseEntity.ok(spuDTO);
    }

    /**
     * 根据brandId品牌id查询商品
     *
     * @param brandId 品牌id
     * @param cid3    分类id
     * @return 查询到的spu的list集合
     */
    @GetMapping("/spu/for/brand")
    public ResponseEntity<List<SpuDTO>> findSpuByBrandId(@RequestParam(name = "id") Long brandId,
                                                         @RequestParam(name = "cid") Long cid3) {
        return ResponseEntity.ok(spuService.findSpuByBrandId(brandId, cid3));
    }
}
