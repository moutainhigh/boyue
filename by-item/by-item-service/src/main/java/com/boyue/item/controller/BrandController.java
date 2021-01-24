package com.boyue.item.controller;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.BrandDTO;
import com.boyue.item.entity.ByBrand;
import com.boyue.item.service.ByBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/17 22:17
 * @Author: Jacky
 * @Description:
 */
@RestController
@Slf4j
@Api("商品服务中心BrandController")
public class BrandController {

    @Autowired
    private ByBrandService brandService;

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
    @ApiOperation(value = "获取品牌列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key",value = "搜索的关键词",required = false,dataType = "String"),
            @ApiImplicitParam(name = "page",value = "当前页码",required = false,dataType = "Integer",defaultValue = "1"),
            @ApiImplicitParam(name = "rows",value = "每页显示条数",required = false,dataType = "Integer",defaultValue = "10"),
            @ApiImplicitParam(name = "sortBy",value = "排序字段",required = false,dataType = "String"),
            @ApiImplicitParam(name = "desc",value = "是否降序",required = false,dataType = "Boolean",defaultValue = "false")
    })
    @GetMapping(path = "/brand/page", name = "获取品牌列表")
    public ResponseEntity<PageResult<BrandDTO>> findCategoryList(@RequestParam(name = "key", required = false) String key,
                                                                 @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                 @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
                                                                 @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                 @RequestParam(name = "desc", required = false, defaultValue = "false") Boolean desc) {
        log.info("调用findCategoryList接口");
        PageResult<BrandDTO> pageResult = brandService.findCategoryList(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 新增品牌信息
     * POST /brand
     *
     * @param brandDTO 品牌的dto对象
     * @param cids     分类id集合
     * @return 空
     */
    @ApiOperation(value = "新增品牌信息")
    @PostMapping(path = "/brand", name = "新增品牌信息")
    public ResponseEntity<Void> saveBrand(BrandDTO brandDTO, @RequestParam(name = "cids") List<Long> cids) {
        log.info("调用 saveBrand 接口");
        brandService.saveBrand(brandDTO, cids);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改品牌信息
     * PUT /brand
     *
     * @param brandDTO 品牌的dto对象
     * @param cids     分类id集合
     * @return 空
     */
    @ApiOperation(value = "修改品牌信息")
    @PutMapping(path = "/brand", name = "修改品牌信息")
    public ResponseEntity<Void> updateBrand(BrandDTO brandDTO, @RequestParam(name = "cids") List<Long> cids) {
        log.info("调用 updateBrand 接口");
        brandService.updateBrand(brandDTO, cids);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据id查询品牌信息
     * GET /brand/{id}
     *
     * @param id 品牌id
     * @return brandDTO对象
     */
    @ApiOperation(value = "根据id查询品牌信息")
    @ApiImplicitParam(name = "id", value = "品牌id", dataType = "String")
    @GetMapping(path = "/brand/{id}", name = "根据id查询品牌信息")
    public ResponseEntity<BrandDTO> findBrandById(@PathVariable(value = "id") String id) {
        log.info("调用 findBrandById 接口");
        BrandDTO brandDTO = brandService.findBrandById(id);
        return ResponseEntity.ok(brandDTO);
    }

    /**
     * 根据id删除品牌信息
     * DELETE http://api.boyue.com/api/item/delete/brand/325403
     *
     * @param id 品牌id
     * @return 空
     */
    @ApiOperation(value = "根据id删除品牌信息")
    @ApiImplicitParam(name = "id", value = "品牌id", dataType = "Long")
    @DeleteMapping(path = "/delete/brand/{id}", name = "根据id删除品牌信息")
    public ResponseEntity<Void> deleteBrandById(@PathVariable(value = "id") Long id) {
        log.info("调用 deleteBrandById 接口");
        brandService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据分类id获取品牌信息
     * GET /brand/of/category?id=76
     *
     * @param id 分类id
     * @return brandDTO对象
     */
    @ApiOperation(value = "根据分类id获取品牌信息")
    @ApiImplicitParam(name = "id", value = "分类id", dataType = "Long")
    @GetMapping(path = "/brand/of/category", name = "根据分类id获取品牌信息")
    public ResponseEntity<List<BrandDTO>> findBrandByCategoryId(@RequestParam(name = "id") Long id) {
        log.info("调用 findBrandByCategoryId 接口");
        List<BrandDTO> brandDTOs = brandService.findBrandByCategoryId(id);
        return ResponseEntity.ok(brandDTOs);
    }

    /**
     * 传递品牌的的ids，获取品牌的集合数据
     * 请求路径   GET /brand/list?ids=27359021572,28359021572
     *
     * @param ids 品牌的id集合
     * @return 查询出来的品牌结果
     */
    @ApiOperation(value = "获取品牌的集合数据")
    @ApiImplicitParam(name = "ids", value = "品牌的id集合", dataType = "String")
    @GetMapping(path = "/brand/list", name = "传递品牌的的ids，获取品牌的集合数据")
    public ResponseEntity<List<BrandDTO>> findBrandByIds(@RequestParam(name = "ids") String ids) {
        log.info("调用 findBrandByIds 接口");
        return ResponseEntity.ok(brandService.findBrandByIds(ids));
    }

    /**
     * 传递品牌的的ids，获取品牌的集合数据
     * 请求路径   GET /brand/listBrand?ids=27359021572,28359021572
     *
     * @param ids 品牌的id集合
     * @return 查询出来的品牌结果
     */
    @ApiOperation(value = "获取品牌的集合数据")
    @ApiImplicitParam(name = "ids", value = "品牌的id集合", dataType = "List<Long>")
    @GetMapping(path = "/brand/listBrand", name = "传递品牌的的ids，获取品牌的集合数据")
    public ResponseEntity<List<BrandDTO>> findBrandListByIds(@RequestParam(name = "ids") List<Long> ids) {
        log.info("调用 findBrandListByIds 接口");

        Collection<ByBrand> brandCollection = brandService.listByIds(ids);
        if (CollectionUtils.isEmpty(brandCollection)) {
            throw new ByException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        List<ByBrand> brandList = (List<ByBrand>) brandCollection;
        List<BrandDTO> brandDTOList = BeanHelper.copyWithCollection(brandList, BrandDTO.class);
        return ResponseEntity.ok(brandDTOList);
    }
}
