package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.item.dto.SkuDTO;
import com.boyue.item.dto.SpuDTO;
import com.boyue.item.dto.SpuDetailDTO;
import com.boyue.item.entity.BySku;
import com.boyue.item.entity.BySpu;
import com.boyue.item.entity.BySpuDetail;
import com.boyue.item.service.BySkuService;
import com.boyue.item.service.BySpuDetailService;
import com.boyue.item.service.BySpuService;
import com.boyue.item.service.GoodsServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/20 20:06
 * @Author: Jacky
 * @Description: 统一管理商品的service接口层
 */
@Service
@Slf4j
public class GoodsServerImpl implements GoodsServer {

    /**
     * 注入spu的service对象
     */
    @Autowired
    private BySpuService spuService;

    /**
     * 注入spuDetailService
     */
    @Autowired
    private BySpuDetailService spuDetailService;

    /**
     * 注入sku的service对象
     */
    @Autowired
    private BySkuService skuService;

    /**
     * 新增商品信息
     * 操作三个表，spu，sku，spu_detail
     *
     * @param spuDTO 商品对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGoods(SpuDTO spuDTO) {
        //判断参数是否合法
        if (spuDTO == null) {
            log.error("******** 传递的spuDTO对象为空 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //优先操作spu表进行保存
        //数据类型转换
        BySpu spu = BeanHelper.copyProperties(spuDTO, BySpu.class);
        //保存spu数据
        boolean spuSaveFlag = spuService.save(spu);
        if (!spuSaveFlag) {
            log.error("******** spu保存未成功 **********");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //操作spuDetail
        //转换数据类型
        BySpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), BySpuDetail.class);
        //设置spuId
        //获取spuId
        Long spuId = spu.getId();
        spuDetail.setSpuId(spuId);
        //保存spuDetail
        boolean spuDetailSaveFlag = spuDetailService.save(spuDetail);
        if (!spuDetailSaveFlag) {
            log.error("******** spuDetail保存未成功 **********");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //操作sku
        List<SkuDTO> skuDTOs = spuDTO.getSkus();
        if (CollectionUtils.isEmpty(skuDTOs)) {
            log.error("******** 没有传递skuDTOs的集合参数 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //数据类型转换
        List<BySku> skus = BeanHelper.copyWithCollection(skuDTOs, BySku.class);
        if (CollectionUtils.isEmpty(skus)) {
            log.error("******** skuDTO对象类型转换异常 **********");
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
        //设置spuId
        for (BySku sku : skus) {
            sku.setSpuId(spuId);
        }
        boolean skuSaveFlag = skuService.saveBatch(skus);
        if (!skuSaveFlag) {
            log.error("******** sku保存未成功 **********");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 修改商品上下架，更新spu信息，同时需要更新sku
     *
     * @param id       商品id
     * @param saleable 上下架状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSaleable(Long id, Boolean saleable) {
        if (id == null || saleable == null) {
            log.error("******** 传递参数id或saleable为空 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        BySpu spu = new BySpu();
        spu.setId(id);
        spu.setSaleable(saleable);
        boolean spuUpdateFlag = spuService.updateById(spu);
        if (!spuUpdateFlag) {
            log.error("******** spu对象下架操作失败 **********");
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        //构造更新条件
        UpdateWrapper<BySku> updateWrapper = new UpdateWrapper<>();
        //设置更新条件
        updateWrapper.lambda().eq(BySku::getSpuId, id).set(BySku::getEnable, saleable);
        boolean skuFlag = skuService.update(updateWrapper);
        if (!skuFlag) {
            log.error("******** sku对象下架操作失败 **********");
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
     * 修改商品，操作三张表
     *
     * @param spuDTO 商品对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(SpuDTO spuDTO) {
        //判断参数是否有效
        if (spuDTO == null) {
            log.error("******** 传递参数spuDTO为空 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //获取spuId
        Long spuId = spuDTO.getId();

        //操作spu
        //修改spu
        BySpu spu = BeanHelper.copyProperties(spuDTO, BySpu.class);
        boolean spuUpdateFlag = spuService.updateById(spu);
        if (!spuUpdateFlag) {
            log.error("******** 修改spu操作失败 **********");
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        //修改spuDetail
        SpuDetailDTO spuDetailDTO = spuDTO.getSpuDetail();
        BySpuDetail spuDetail = BeanHelper.copyProperties(spuDetailDTO, BySpuDetail.class);
        boolean spuDetailUpdateFlag = spuDetailService.updateById(spuDetail);
        if (!spuDetailUpdateFlag) {
            log.error("******** 修改spuDetail操作失败 **********");
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        //操作sku
        //通过spuId删除sku表中之前的数据
        this.removeSku(spuId);

        //新增sku
        //获取spuDTO中的sku集合
        List<SkuDTO> skuDTOList = spuDTO.getSkus();
        if (CollectionUtils.isEmpty(skuDTOList)) {
            log.error("******** 获取到的sku对象是空的 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //添加到数据库
        //数据类型转换
        List<BySku> skuList = BeanHelper.copyWithCollection(skuDTOList, BySku.class);
        if (CollectionUtils.isEmpty(skuList)) {
            log.error("******** skuList类型转换异常 **********");
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        //设置spuId
        for (BySku sku : skuList) {
            sku.setSpuId(spuId);
        }
        boolean skuSaveFlag = skuService.saveBatch(skuList);
        if (!skuSaveFlag) {
            log.error("******** 保存sku操作失败 **********");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 根据id删除商品spu数据
     * 需要删除三张表
     *
     * @param id 品牌id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpuById(Long id) {
        //判断参数是否合法
        if (id == null) {
            log.error("******** 传递的id为空 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //先删除spu
        boolean spuFlag = spuService.removeById(id);
        if (!spuFlag) {
            log.error("******** 删除spu操作失败 **********");
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        //删除spuDetail
        boolean spuDetailFlag = spuDetailService.removeById(id);
        if (!spuDetailFlag) {
            log.error("******** 删除spuDetail操作失败 **********");
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        //删除sku
        this.removeSku(id);
    }

    /**
     * 删除sku的方法
     *
     * @param spuId spuId
     */
    private void removeSku(Long spuId) {
        QueryWrapper<BySku> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BySku::getSpuId, spuId);
        boolean skuFlag = skuService.remove(queryWrapper);
        if (!skuFlag) {
            log.error("******** 删除sku操作失败 **********");
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }
}
