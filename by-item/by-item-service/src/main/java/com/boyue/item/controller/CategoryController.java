package com.boyue.item.controller;

import com.boyue.item.dto.CategoryDTO;
import com.boyue.item.service.ByCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/16 23:04
 * @Author: Jacky
 * @Description:
 */
@RestController
@Slf4j
@Api("商品服务中心CategoryController")
public class CategoryController {

    @Autowired
    private ByCategoryService categoryService;

    /**
     * 功能说明： 传递商品分类的父id值，获取属于这个父id的所有分类信息
     * 接口路径： GET /category/of/parent?pid=0
     *
     * @param pid 分类的父id
     * @return 查询到的分类的集合
     */
    @ApiOperation(value = "获取属于这个父id的所有分类信息")
    @ApiImplicitParam(name = "pid", value = "分类的父id", dataType = "Long")
    @GetMapping(path = "/category/of/parent", name = "传递商品分类的父id值，获取属于这个父id的所有分类信息")
    public ResponseEntity<List<CategoryDTO>> findCategoryByPid(@RequestParam(name = "pid", defaultValue = "0") Long pid) {
        log.info("[item-service服务]findCategoryByPid接口接收到请求, 获取属于这个父id的所有分类信息");
        List<CategoryDTO> categoryList = categoryService.findCategoryByPid(pid);
        return ResponseEntity.ok(categoryList);
    }

    /**
     * 功能说明:  传分类id集合，获取分类集合
     * 接口路径:  GET /category/list?ids=73,74,75
     *
     * @param ids 分类id的集合
     * @return 查询到的分类的集合
     */
    @ApiOperation(value = "获取分类集合findCategoryByIds")
    @ApiImplicitParam(name = "ids", value = "分类id的集合", dataType = "List<Long>")
    @GetMapping(path = "/category/list", name = "传分类id集合，获取分类集合")
    public ResponseEntity<List<CategoryDTO>> findCategoryByIds(@RequestParam(name = "ids") List<Long> ids) {
        log.info("[item-service服务]findCategoryByIds接口接收到请求, 获取分类集合");
        return ResponseEntity.ok(categoryService.findCategoryByIds(ids));
    }

    /**
     * 获取分类信息
     * GET /category/of/brand/?id=325406
     *
     * @param brandId 品牌id
     * @return 查询到的categoryDTO对象的集合
     */
    @ApiOperation(value = "获取分类集合findCategoryByBrandId")
    @ApiImplicitParam(name = "brandId", value = "品牌id", dataType = "Long")
    @GetMapping(path = "/category/of/brand", name = "获取分类信息")
    public ResponseEntity<List<CategoryDTO>> findCategoryByBrandId(@RequestParam(name = "id") Long brandId) {
        log.info("[item-service服务]findCategoryByBrandId接口接收到请求, 获取分类集合");
        List<CategoryDTO> categoryList = categoryService.findCategoryById(brandId);
        return ResponseEntity.ok(categoryList);
    }

    /**
     * 修改分类信息
     * PUT http://api.boyue.com/api/item/category
     *
     * @param categoryDTO 分类对象
     * @return 空
     */
    @ApiOperation(value = "修改分类信息")
    @PutMapping(path = "/category", name = "修改分类信息")
    public ResponseEntity<Void> updateCategoryById(@RequestBody CategoryDTO categoryDTO) {
        log.info("[item-service服务]updateCategoryById接口接收到请求, 修改分类信息");
        log.info("categoryDTO={}",categoryDTO);
        categoryService.updateCategoryById(categoryDTO);

        return ResponseEntity.noContent().build();
    }

    /**
     * 添加分类信息
     * POST http://api.boyue.com/api/item/category 405
     *
     * @param categoryDTO categoryDTO 分类对象
     * @return 空
     */
    @ApiOperation(value = "添加分类信息")
    @PostMapping(path = "/category", name = "添加分类信息")
    public ResponseEntity<Void> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("[item-service服务]saveCategory接口接收到请求, 添加分类信息");
        log.info("categoryDTO={}",categoryDTO);
        categoryService.saveCategory(categoryDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除分类
     * DELETE http://api.boyue.com/api/item/category?id=1424
     * @param id 分类id
     * @return 空
     */
    @ApiOperation(value = "删除分类")
    @ApiImplicitParam(name = "id", value = "分类id", dataType = "Long")
    @DeleteMapping(path = "/category", name = "删除分类信息")
    public ResponseEntity<Void> deleteCategory(@RequestParam(name = "id") Long id){
        log.info("[item-service服务]deleteCategory接口接收到请求, 删除分类信息");
        categoryService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 创建父分类信息
     * POST http://api.boyue.com/api/item/newCategory
     *
     * @param categoryDTO categoryDTO 分类对象
     * @return 空
     */
    @ApiOperation(value = "创建父分类信息")
    @PostMapping(path = "/newCategory", name = "添加分类信息")
    public ResponseEntity<Void> createCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("[item-service服务]createCategory接口接收到请求, 添加分类信息");
        log.info("categoryDTO={}",categoryDTO);
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.noContent().build();
    }
}
