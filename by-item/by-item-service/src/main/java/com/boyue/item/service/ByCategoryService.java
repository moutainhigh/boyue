package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.item.dto.CategoryDTO;
import com.boyue.item.entity.ByCategory;

import java.util.List;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByCategoryService extends IService<ByCategory> {
    /**
     * 功能说明： 传递商品分类的父id值，获取属于这个父id的所有分类信息
     *
     * @param pid 分类的父id
     * @return 查询到的分类的集合
     */
    List<CategoryDTO> findCategoryByPid(Long pid);

    /**
     * 功能说明:  传分类id集合，获取分类集合
     *
     * @param ids 分类id的集合
     * @return 查询到的分类的集合
     */
    List<CategoryDTO> findCategoryByIds(List<Long> ids);

    /**
     * 获取分类信息
     *
     * @param brandId 品牌id
     * @return 查询到的categoryDTO对象的集合
     */
    List<CategoryDTO> findCategoryById(Long brandId);

    /**
     * 修改分类信息
     *
     * @param categoryDTO 分类对象
     */
    void updateCategoryById(CategoryDTO categoryDTO);

    /**
     * 添加分类信息
     *
     * @param categoryDTO categoryDTO 分类对象
     */
    void saveCategory(CategoryDTO categoryDTO);
}
