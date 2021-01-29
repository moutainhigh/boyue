package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.item.dto.SkuDTO;
import com.boyue.item.entity.BySku;
import com.boyue.item.mapper.BySkuMapper;
import com.boyue.item.service.BySkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
@Slf4j
public class BySkuServiceImpl extends ServiceImpl<BySkuMapper, BySku> implements BySkuService {

    /**
     * 根据spu的id查询Sku集合接口
     *
     * @param id 商品实体id
     * @return 商品实体
     */
    @Override
    public List<SkuDTO> findSkuBySpuId(Long id) {
        if (id == null) {
            log.error("******** 传递参数id为空 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        QueryWrapper<BySku> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BySku::getSpuId, id);
        List<BySku> skuList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(skuList)) {
            log.error("******** 查询结果为空，没有sku数据 **********");
            throw new ByException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        return BeanHelper.copyWithCollection(skuList, SkuDTO.class);
    }

    /**
     * 根据skuId的List集合查询sku集合
     *
     * @param ids skuId的集合
     * @return sku的集合
     */
    @Override
    public List<SkuDTO> findSkuByListIds(List<Long> ids) {
        //判断参数是否有效
        if (CollectionUtils.isEmpty(ids)){
            log.error("******** 传递参数ids为空**********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //获取结果集
        List<BySku> skuList = (List<BySku>)this.listByIds(ids);
        if (CollectionUtils.isEmpty(skuList)){
            log.error("******** 查询出的结果为NULL，商品不存在 **********");
            throw new ByException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //类型转换
        List<SkuDTO> skuDTOList = BeanHelper.copyWithCollection(skuList, SkuDTO.class);
        if (CollectionUtils.isEmpty(skuDTOList)) {
            log.error("******** 类型转换失败！！！ **********");
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        return skuDTOList;
    }

    /**
     * 传递sku的ids，获取sku的集合数据
     *
     * @param ids sku的id集合，如有多个用逗号分隔
     * @return skuDTO的集合
     */
    @Override
    public List<SkuDTO> findSkuByIds(String ids) {
        //判断参数是否有效
        if (StringUtils.isEmpty(ids)){
            log.error("******** 传递参数ids为空**********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //处理ids
        List<String> skuIds = Collections.singletonList(ids);

        //获取结果集
        List<BySku> skuList = (List<BySku>)this.listByIds(skuIds);
        if (CollectionUtils.isEmpty(skuList)){
            log.error("******** 查询出的结果为NULL，商品不存在 **********");
            throw new ByException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //类型转换
        List<SkuDTO> skuDTOList = BeanHelper.copyWithCollection(skuList, SkuDTO.class);
        if (CollectionUtils.isEmpty(skuDTOList)) {
            log.error("******** 类型转换失败！！！ **********");
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        return skuDTOList;
    }

    /**
     * 减库存
     *
     * @param skuId  商品id
     * @param number 商品数量
     * @return 保存结果
     */
    @Override
    public int minusStock(Long skuId, Integer number) {
        if (skuId == null || number == null){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        return this.getBaseMapper().minusStock(skuId,number);
    }

    /**
     * 加库存
     *
     * @param skuId 商品id
     * @param number 数量
     * @return 返回值
     */
    @Override
    public int plusStock(Long skuId, Integer number) {
        if (skuId == null || number == null){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        return this.getBaseMapper().plusStock(skuId,number);
    }
}
