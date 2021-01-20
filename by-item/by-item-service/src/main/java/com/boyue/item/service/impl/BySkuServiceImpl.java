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
}