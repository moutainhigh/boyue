package com.boyue.item.controller;

import com.boyue.item.dto.CategoryDTO;
import com.boyue.item.service.ByCategoryService;
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
    @GetMapping(path = "/category/of/parent", name = "传递商品分类的父id值，获取属于这个父id的所有分类信息")
    public ResponseEntity<List<CategoryDTO>> findCategoryByPid(@RequestParam(name = "pid", defaultValue = "0") Long pid) {
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
    @GetMapping(path = "/category/list", name = "传分类id集合，获取分类集合")
    public ResponseEntity<List<CategoryDTO>> findCategoryByIds(@RequestParam(name = "ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.findCategoryByIds(ids));
    }

    /**
     * 获取分类信息
     * GET /category/of/brand/?id=325406
     *
     * @param brandId 品牌id
     * @return 查询到的categoryDTO对象的集合
     */
    @GetMapping(path = "/category/of/brand", name = "获取分类信息")
    public ResponseEntity<List<CategoryDTO>> findCategoryByBrandId(@RequestParam(name = "id") Long brandId) {
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
    @PutMapping(path = "/category", name = "修改分类信息")
    public ResponseEntity<Void> updateCategoryById(@RequestBody CategoryDTO categoryDTO) {
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
    @PostMapping(path = "/category", name = "添加分类信息")
    public ResponseEntity<Void> saveCategory(@RequestBody CategoryDTO categoryDTO) {
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
    @DeleteMapping(path = "/category", name = "删除分类信息")
    public ResponseEntity<Void> deleteCategory(@RequestParam(name = "id") Long id){
        categoryService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 创建夫分类信息
     * POST http://api.boyue.com/api/item/newCategory
     *
     * @param categoryDTO categoryDTO 分类对象
     * @return 空
     */
    @PostMapping(path = "/newCategory", name = "添加分类信息")
    public ResponseEntity<Void> createCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("categoryDTO={}",categoryDTO);
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.noContent().build();
    }
}
