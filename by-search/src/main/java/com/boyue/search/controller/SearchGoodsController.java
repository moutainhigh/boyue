package com.boyue.search.controller;

import com.boyue.common.vo.PageResult;
import com.boyue.search.dto.GoodsDTO;
import com.boyue.search.dto.SearchRequest;
import com.boyue.search.service.SearchGoodsService;
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
public class SearchGoodsController {

    @Autowired
    private SearchGoodsService searchGoodsService;

    /**
     * 商城关键词搜索
     * POST /page
     * @param searchRequest 请求参数
     * @return 分页查询结果
     */
    @PostMapping(path = "/page",name = "商城关键词搜索")
    public ResponseEntity<PageResult<GoodsDTO>> searchByKey(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(searchGoodsService.findSearchByKey(searchRequest));
    }

    /**
     * 商城查询过滤条件
     * 请求路径  POST /filter
     *
     * @param searchRequest 请求参数封装的对象  key -- 搜索关键字  page--当前页码
     * @return Map<String, List> 查询的map集合结果集
     */
    @PostMapping(path = "/filter", name = "商城查询过滤条件")
    public ResponseEntity<Map<String, List<?>>> searchFilter(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(searchGoodsService.searchGoodsFilter(searchRequest));
    }
}
