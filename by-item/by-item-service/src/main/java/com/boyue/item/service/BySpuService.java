package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.SpuDTO;
import com.boyue.item.entity.BySpu;

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
}
