package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.BrandDTO;
import com.boyue.item.entity.ByBrand;

import java.util.List;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByBrandService extends IService<ByBrand> {

    /**
     * 功能说明:  获取品牌列表
     *
     * @param key    搜索的关键词
     * @param page   当前页码
     * @param rows   每页显示条数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @return 查询到的品牌的对象集合
     */
    PageResult<BrandDTO> findCategoryList(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 新增品牌信息
     *
     * @param brandDTO 品牌的dto对象
     * @param cids     分类id集合
     */
    void saveBrand(BrandDTO brandDTO, List<Long> cids);

    /**
     * 修改品牌信息
     *
     * @param brandDTO 品牌的dto对象
     * @param cids     分类id集合
     */
    void updateBrand(BrandDTO brandDTO, List<Long> cids);

    /**
     * 根据id查询品牌信息
     *
     * @param id 品牌id
     * @return brandDTO对象
     */
    BrandDTO findBrandById(String id);

    /**
     * 根据分类id获取品牌信息
     *
     * @param id 分类id
     * @return brandDTO对象
     */
    List<BrandDTO> findBrandByCategoryId(Long id);

    /**
     * 传递品牌的的ids，获取品牌的集合数据
     *
     * @param brandIds 品牌的id集合
     * @return 查询出来的品牌结果
     */
    List<BrandDTO> findBrandByIds(String brandIds);
}
