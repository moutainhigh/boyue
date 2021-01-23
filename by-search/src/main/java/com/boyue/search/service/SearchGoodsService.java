package com.boyue.search.service;

import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.SpuDTO;
import com.boyue.search.dto.GoodsDTO;
import com.boyue.search.dto.SearchRequest;
import com.boyue.search.entity.Goods;

import java.util.List;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/21 19:03
 * @Author: Jacky
 * @Description: 搜索商品的service接口
 */
public interface SearchGoodsService {
    /**
     * 构造goods对象
     * 将spuDTO对象转换为Goods对象
     *
     * @param spuDTO 抽象的商品对象
     * @return goods对象
     */
    Goods createGoods(SpuDTO spuDTO);

    /**
     * 商城关键词搜索,key查询
     * POST /page
     *
     * @param searchRequest 请求参数
     * @return 分页查询结果
     */
    PageResult<GoodsDTO> findSearchByKey(SearchRequest searchRequest);

    /**
     * 商城查询过滤条件
     * 请求路径  POST /filter
     *
     * @param searchRequest 请求参数封装的对象  key -- 搜索关键字  page--当前页码
     * @return Map<String, List> 查询的map集合结果集
     */
    Map<String, List<?>> searchGoodsFilter(SearchRequest searchRequest);

    /**
     * 添加新的goods对象到es
     *
     * @param id 商品id
     */
    void createIndex(Long id);

    /**
     * 商品下架时删除es中的索引
     *
     * @param id 商品id
     */
    void removeIndex(Long id);
}
