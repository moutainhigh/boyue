package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.SpuDTO;
import com.boyue.item.entity.ByBrand;
import com.boyue.item.entity.ByCategory;
import com.boyue.item.entity.BySpu;
import com.boyue.item.mapper.BySpuMapper;
import com.boyue.item.service.ByBrandService;
import com.boyue.item.service.ByCategoryService;
import com.boyue.item.service.BySpuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
@Slf4j
public class BySpuServiceImpl extends ServiceImpl<BySpuMapper, BySpu> implements BySpuService {

    @Autowired
    private ByBrandService brandService;

    @Autowired
    private ByCategoryService categoryService;

    /**
     * 查询商品SPU信息 ，分页查询
     *
     * @param page     当前页
     * @param rows     每页显示条数
     * @param key      过滤条件
     * @param saleable 上架或下架
     * @return pageResult对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<SpuDTO> findAllOfSpu(Integer page, Integer rows, String key, Boolean saleable) {
        //构造分页查询条件
        IPage<BySpu> iPage = new Page<>(page, rows);
        //构造查询条件
        QueryWrapper<BySpu> queryWrapper = new QueryWrapper<>();

        //设置查询条件
        //判断是否含有过滤条件
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.lambda().like(BySpu::getName, key);
        }
        //判断上下架条件
        if (saleable != null) {
            queryWrapper.lambda().eq(BySpu::getSaleable, saleable);
        }

        //分页查询
        IPage<BySpu> spuIPage = this.page(iPage, queryWrapper);

        //判断查询结果集
        if (spuIPage == null || CollectionUtils.isEmpty(spuIPage.getRecords())) {
            log.error("商品spu数据查询结果为空");
            throw new ByException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //处理查询结果集，封装成pageResult对象
        //获取分页查询出的各参数
        List<BySpu> spuList = spuIPage.getRecords();
        //过去总页数
        long pages = spuIPage.getPages();
        //获取总条数
        long total = spuIPage.getTotal();
        //类型转换
        List<SpuDTO> spuDTOList = BeanHelper.copyWithCollection(spuList, SpuDTO.class);
        if (CollectionUtils.isEmpty(spuDTOList)) {
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        this.handlerSpuCategoryAndBrandName(spuDTOList);

        return new PageResult<>(spuDTOList, total, pages);
    }

    /**
     * 设置商品SpuDTO的品牌名称和分类名称
     *
     * @param spuDTOList spu商品的list集合
     */
    private void handlerSpuCategoryAndBrandName(List<SpuDTO> spuDTOList) {
        for (SpuDTO spuDTO : spuDTOList) {
            //查询商品的品牌名称
            ByBrand brand = brandService.getById(spuDTO.getBrandId());
            spuDTO.setBrandName(brand.getName());
            //获取分类集合的名称
            List<Long> ids = spuDTO.getIds();
            Collection<ByCategory> categories = categoryService.listByIds(ids);
            //设置分类集合，以/分隔
            String categoryName = categories.stream().map(ByCategory::getName).collect(Collectors.joining("/"));
            spuDTO.setCategoryName(categoryName);
        }
    }
}
