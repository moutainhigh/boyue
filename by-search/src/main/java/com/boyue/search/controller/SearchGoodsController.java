package com.boyue.search.controller;

import com.boyue.common.vo.PageResult;
import com.boyue.search.dto.GoodsDTO;
import com.boyue.search.dto.SearchRequest;
import com.boyue.search.service.SearchGoodsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/22 17:00
 * @Author: Jacky
 * @Description: 搜索商品的controller层
 */
@RestController
@Api("商城搜索中心SearchGoodsController")
@Slf4j
public class SearchGoodsController {

    /**
     * 注入searchGoodsService
     */
    @Autowired
    private SearchGoodsService searchGoodsService;

    /**
     * 商城关键词搜索
     * POST /page
     * @param searchRequest 请求参数
     * @return 分页查询结果
     */
    @ApiOperation(value = "商城关键词搜索")
    @PostMapping(path = "/page",name = "商城关键词搜索")
    public ResponseEntity<PageResult<GoodsDTO>> searchByKey(@RequestBody SearchRequest searchRequest){
        log.info("[by-search服务]searchByKey接口接收到请求,searchRequest={}",searchRequest);
        return ResponseEntity.ok(searchGoodsService.findSearchByKey(searchRequest));
    }

    /**
     * 商城查询过滤条件
     * 请求路径  POST /filter
     *
     * @param searchRequest 请求参数封装的对象  key -- 搜索关键字  page--当前页码
     * @return Map<String, List> 查询的map集合结果集
     */
    @ApiOperation(value = "商城查询过滤条件")
    @PostMapping(path = "/filter", name = "商城查询过滤条件")
    public ResponseEntity<Map<String, List<?>>> searchFilter(@RequestBody SearchRequest searchRequest) {
        log.info("[by-search服务]searchFilter接口接收到请求,searchRequest={}",searchRequest);
        return ResponseEntity.ok(searchGoodsService.searchGoodsFilter(searchRequest));
    }
}
