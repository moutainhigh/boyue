package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.item.dto.CategoryDTO;
import com.boyue.item.entity.ByCategory;
import com.boyue.item.mapper.ByCategoryMapper;
import com.boyue.item.service.ByCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
@Slf4j
public class ByCategoryServiceImpl extends ServiceImpl<ByCategoryMapper, ByCategory> implements ByCategoryService {
    /**
     * 功能说明： 传递商品分类的父id值，获取属于这个父id的所有分类信息
     *
     * @param pid 分类的父id
     * @return 查询到的分类的集合
     */
    @Override
    public List<CategoryDTO> findCategoryByPid(Long pid) {
        //校验参数是否正确
        if (pid == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //构造查询条件
        QueryWrapper<ByCategory> queryWrapper = new QueryWrapper<>();
        //添加查询条件
        queryWrapper.lambda().eq(ByCategory::getParentId, pid);
        //查询集合
        List<ByCategory> tbCategoryList = this.list(queryWrapper);
        //校验查询结果是否正确
        if (CollectionUtils.isEmpty(tbCategoryList)) {
            throw new ByException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        //转换为dto类型的数据返回
        List<CategoryDTO> categoryDTOList = BeanHelper.copyWithCollection(tbCategoryList, CategoryDTO.class);
        if (CollectionUtils.isEmpty(categoryDTOList)) {
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        return categoryDTOList;
    }

    /**
     * 功能说明:  传分类id集合，获取分类集合
     *
     * @param ids 分类id的集合
     * @return 查询到的分类的集合
     */
    @Override
    public List<CategoryDTO> findCategoryByIds(List<Long> ids) {
        //1.判断参数是否合法
        if (CollectionUtils.isEmpty(ids)) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        List<ByCategory> categoryList = (List<ByCategory>) this.listByIds(ids);
        //判断查询到的结果是否为空
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new ByException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        //转换成categoryDTO对象
        List<CategoryDTO> categoryDTOS = BeanHelper.copyWithCollection(categoryList, CategoryDTO.class);
        if (CollectionUtils.isEmpty(categoryDTOS)) {
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
        return categoryDTOS;
    }

    /**
     * 获取分类信息
     *
     * @param brandId 品牌id
     * @return 查询到的categoryDTO对象的集合
     */
    @Override
    public List<CategoryDTO> findCategoryById(Long brandId) {
        //判断传递的参数是否有效
        if (brandId == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //根据品牌id进行两表联查
        List<ByCategory> categoryList = this.getBaseMapper().selectCategoryByBrandId(brandId);
        if (CollectionUtils.isEmpty(categoryList)){
            throw new ByException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }

        //转换类型
        List<CategoryDTO> categoryDTOList = BeanHelper.copyWithCollection(categoryList, CategoryDTO.class);

        if (CollectionUtils.isEmpty(categoryDTOList)){
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        return categoryDTOList;
    }

    /**
     * 修改分类信息
     *
     * @param categoryDTO 分类对象
     */
    @Override
    public void updateCategoryById(CategoryDTO categoryDTO) {
        //构造条件
        UpdateWrapper<ByCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(ByCategory::getName,categoryDTO.getName()).eq(ByCategory::getId,categoryDTO.getId());
        boolean updateFlag = this.update(updateWrapper);
        if (!updateFlag){
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
     * 添加分类信息
     *
     * @param categoryDTO categoryDTO 分类对象
     */
    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        ByCategory category = BeanHelper.copyProperties(categoryDTO, ByCategory.class);
        boolean flag = this.save(category);
        if (!flag){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 创建夫分类信息
     *
     * @param categoryDTO categoryDTO 分类对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //类型转换
        ByCategory category = BeanHelper.copyProperties(categoryDTO, ByCategory.class);
        //构造分类对象
        //select count(id) from by_category where parent_id = 0;
        QueryWrapper<ByCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(id) as total").lambda().eq(ByCategory::getParentId,0);
        Map<String, Object> map = this.getMap(queryWrapper);
        Long value = (Long) map.get("total");
        int total = value.intValue();
        total++;
        //设置sort
       category.setSort(total);
       log.info("构造后的category={}",category);
        boolean saveFlag = this.save(category);
        if (!saveFlag){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }
}
