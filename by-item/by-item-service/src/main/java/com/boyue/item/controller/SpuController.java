package com.boyue.item.controller;

import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.SpuDTO;
import com.boyue.item.service.BySpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/19 20:11
 * @Author: Jacky
 * @Description: spu的controller接口层
 */
@RestController
@Slf4j
public class SpuController {

    @Autowired
    private BySpuService spuService;

    /**
     * 查询商品SPU信息 ，分页查询
     * GET /spu/page
     * @param page 当前页
     * @param rows 每页显示条数
     * @param key 过滤条件
     * @param saleable 上架或下架
     * @return pageResult对象
     */
    @GetMapping(path = "/spu/page",name = "查询商品SPU信息 ，分页查询")
    public ResponseEntity<PageResult<SpuDTO>> findAllOfSpu(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
                                                         @RequestParam(name = "rows",required = false,defaultValue = "5") Integer rows,
                                                         @RequestParam(name = "key",required = false) String key,
                                                         @RequestParam(name = "saleable",required = false) Boolean saleable){
        log.info("查询商品SPU信息  findAllOfSpu");
        PageResult<SpuDTO> spuDTO = spuService.findAllOfSpu(page,rows,key,saleable);
        return ResponseEntity.ok(spuDTO);
    }
}
