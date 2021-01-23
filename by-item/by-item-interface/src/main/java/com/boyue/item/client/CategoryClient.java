package com.boyue.item.client;

import com.boyue.item.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/16 23:04
 * @Author: Jacky
 * @Description: 分类的client接口
 */
@FeignClient("item-service")
public interface CategoryClient {
    /**
     * 功能说明： 传递商品分类的父id值，获取属于这个父id的所有分类信息
     * 接口路径： GET /category/of/parent?pid=0
     *
     * @param pid 分类的父id
     * @return 查询到的分类的集合
     */
    @GetMapping(path = "/category/of/parent", name = "传递商品分类的父id值，获取属于这个父id的所有分类信息")
    List<CategoryDTO> findCategoryByPid(@RequestParam(name = "pid", defaultValue = "0") Long pid);

    /**
     * 功能说明:  传分类id集合，获取分类集合
     * 接口路径:  GET /category/list?ids=73,74,75
     *
     * @param ids 分类id的集合
     * @return 查询到的分类的集合
     */
    @GetMapping(path = "/category/list", name = "传分类id集合，获取分类集合")
    List<CategoryDTO> findCategoryByIds(@RequestParam(name = "ids") List<Long> ids);

    /**
     * 获取分类信息
     * GET /category/of/brand/?id=325406
     *
     * @param brandId 品牌id
     * @return 查询到的categoryDTO对象的集合
     */
    @GetMapping(path = "/category/of/brand", name = "获取分类信息")
    List<CategoryDTO> findCategoryByBrandId(@RequestParam(name = "id") Long brandId);
}
