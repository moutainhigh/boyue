package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.BrandDTO;
import com.boyue.item.entity.ByBrand;
import com.boyue.item.entity.ByCategoryBrand;
import com.boyue.item.mapper.ByBrandMapper;
import com.boyue.item.service.ByBrandService;
import com.boyue.item.service.ByCategoryBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class ByBrandServiceImpl extends ServiceImpl<ByBrandMapper, ByBrand> implements ByBrandService {

    @Autowired
    private ByCategoryBrandService categoryBrandService;

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
    @Override
    public PageResult<BrandDTO> findCategoryList(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //构造分页查询条件
        IPage<ByBrand> iPage = new Page<>(page, rows);
        //构造查询条件
        QueryWrapper<ByBrand> queryWrapper = new QueryWrapper<>();
        //添加查询条件
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.lambda().like(ByBrand::getName, key).or().like(ByBrand::getLetter, key);
        }
        if (!StringUtils.isNotBlank(sortBy)) {
            if (desc) {
                queryWrapper.orderByDesc(sortBy);
            } else {
                queryWrapper.orderByAsc(sortBy);
            }
        }
        //查询
        IPage<ByBrand> brandIPage = this.page(iPage, queryWrapper);
        //获取查询结果集
        List<ByBrand> brandList = brandIPage.getRecords();
        if (CollectionUtils.isEmpty(brandList)) {
            throw new ByException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        //转换类型
        List<BrandDTO> brandDTOS = BeanHelper.copyWithCollection(brandList, BrandDTO.class);
        if (CollectionUtils.isEmpty(brandDTOS)) {
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        //获取总条数
        long total = brandIPage.getTotal();
        //获取总页数
        long pages = brandIPage.getPages();

        return new PageResult<BrandDTO>(brandDTOS, total, pages);
    }

    /**
     * 新增品牌信息
     * 需要操作 品牌表 和 分类品牌中间表
     *
     * @param brandDTO 品牌的dto对象
     * @param cids     分类id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBrand(BrandDTO brandDTO, List<Long> cids) {
        //判断传递的参数是否合法
        if (brandDTO == null || CollectionUtils.isEmpty(cids)) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //1.保存品牌
        //1.1 类型转换，将brandDTO转换为ByBrand
        ByBrand byBrand = BeanHelper.copyProperties(brandDTO, ByBrand.class);
        //1.2 保存到数据库
        boolean brandFlag = this.save(byBrand);
        if (!brandFlag) {
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //2.保存品牌和分类的中间表
        //2.1 获取brand保存到数据库的自增id
        Long brandId = byBrand.getId();
        //2.2 循环构建categoryBrand对象
        //2.2.1 创建arrayList集合存储分类对象
        ArrayList<ByCategoryBrand> categoryBrands = new ArrayList<>();
        //2.2.2 循环遍历cids
        for (Long cid : cids) {
            //2.2.3 构建categoryBrand对象
            ByCategoryBrand categoryBrand = new ByCategoryBrand();
            categoryBrand.setBrandId(brandId);
            categoryBrand.setCategoryId(cid);
            categoryBrands.add(categoryBrand);
        }

        //2.3 批量保存到数据库
        boolean flag = categoryBrandService.saveBatch(categoryBrands);
        if (!flag) {
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 修改品牌信息
     *
     * @param brandDTO 品牌的dto对象
     * @param cids     分类id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBrand(BrandDTO brandDTO, List<Long> cids) {
        //获取brand的id
        Long brandId = brandDTO.getId();
        //修改品牌
        ByBrand byBrand = BeanHelper.copyProperties(brandDTO, ByBrand.class);
        boolean brandFlag = this.updateById(byBrand);
        if (!brandFlag){
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        //删除categoryBrand
        QueryWrapper<ByCategoryBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ByCategoryBrand::getBrandId,brandId);
        boolean removeFlag = categoryBrandService.remove(queryWrapper);
        if (!removeFlag) {
             throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        //重新保存中间表categoryBrand
        //2.2 循环构建categoryBrand对象
        //2.2.1 创建arrayList集合存储分类对象
        ArrayList<ByCategoryBrand> categoryBrands = new ArrayList<>();
        //2.2.2 循环遍历cids
        for (Long cid : cids) {
            //2.2.3 构建categoryBrand对象
            ByCategoryBrand categoryBrand = new ByCategoryBrand();
            categoryBrand.setBrandId(brandId);
            categoryBrand.setCategoryId(cid);
            categoryBrands.add(categoryBrand);
        }

        //2.3 批量保存到数据库
        boolean flag = categoryBrandService.saveBatch(categoryBrands);
        if (!flag) {
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

    }

    /**
     * 根据id查询品牌信息
     *
     * @param id 品牌id
     * @return brandDTO对象
     */
    @Override
    public BrandDTO findBrandById(String id) {
        if (StringUtils.isEmpty(id)){
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
        ByBrand brand = this.getById(id);
        if (brand == null){
            throw new ByException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyProperties(brand, BrandDTO.class);
    }

    /**
     * 根据分类id获取品牌信息
     *
     * @param id 分类id
     * @return brandDTO对象
     */
    @Override
    public List<BrandDTO> findBrandByCategoryId(Long id) {
        //判断参数是否合法
        if (id == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //构造查询条件对象
        List<ByBrand> brandList =  this.getBaseMapper().findBrandByCategoryId(id);
        if(CollectionUtils.isEmpty(brandList)){
            throw new ByException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(brandList,BrandDTO.class);
    }
}
