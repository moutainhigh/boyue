package com.boyue.item.client;

import com.boyue.item.dto.BrandDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/21 14:54
 * @Author: Jacky
 * @Description: 品牌表的feignClient接口
 */
@FeignClient("item-service")
public interface BrandClient {
    /**
     * 功能说明:  获取品牌列表
     * GET /brand/page?key=&page=1&rows=5&sortBy=id&desc=false
     *
     * @param key    搜索的关键词
     * @param page   当前页码
     * @param rows   每页显示条数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @return 查询到的品牌的对象集合
     */
    @GetMapping(path = "/brand/page", name = "获取品牌列表")
    BrandDTO findCategoryList(@RequestParam(name = "key", required = false) String key,
                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
                              @RequestParam(name = "sortBy", required = false) String sortBy,
                              @RequestParam(name = "desc", required = false, defaultValue = "false") Boolean desc);

    /**
     * 根据id查询品牌信息
     * GET /brand/{id}
     *
     * @param id 品牌id
     * @return brandDTO对象
     */
    @GetMapping(path = "/brand/{id}", name = "根据id查询品牌信息")
    BrandDTO findBrandById(@PathVariable(value = "id") String id);

    /**
     * 根据分类id获取品牌信息
     * GET /brand/of/category?id=76
     *
     * @param id 分类id
     * @return brandDTO对象
     */
    @GetMapping(path = "/brand/of/category", name = "根据分类id获取品牌信息")
    List<BrandDTO> findBrandByCategoryId(@RequestParam(name = "id") Long id);

    /**
     * 传递品牌的的ids，获取品牌的集合数据
     * 请求路径   GET /brand/listBrand?ids=27359021572,28359021572
     * @param ids 品牌的id集合
     * @return 查询出来的品牌结果
     */
    @GetMapping(path = "/brand/listBrand",name = "传递品牌的的ids，获取品牌的集合数据")
    List<BrandDTO> findBrandListByIds(@RequestParam(name = "ids")List<Long> ids);

    /**
     * 通过brandId查询品牌信息
     * @param id 品牌id
     * @return 品牌对象
     */
    @GetMapping(path = "/brand/{id}",name = "通过brandId查询brand")
    BrandDTO findBrandListById(@PathVariable(name = "id") Long id);
}
