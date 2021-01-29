package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.SpuDTO;
import com.boyue.item.entity.BySpu;

import java.util.List;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface BySpuService extends IService<BySpu> {

    /**
     * 查询商品SPU信息 ，分页查询
     *
     * @param page     当前页
     * @param rows     每页显示条数
     * @param key      过滤条件
     * @param saleable 上架或下架
     * @return pageResult对象
     */
    PageResult<SpuDTO> findAllOfSpu(Integer page, Integer rows, String key, Boolean saleable);

    /**
     * 根据主键id查询sou信息
     * @param id 主键id
     * @return spuDTO对象
     */
    SpuDTO findSpuById(Long id);

    /**
     * 根据brandId品牌id查询商品
     *
     * @param brandId 品牌id
     * @param cid3    分类id
     * @return 查询到的spu的list集合
     */
    List<SpuDTO> findSpuByBrandId(Long brandId, Long cid3);
}
