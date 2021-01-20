package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.item.dto.SpuDetailDTO;
import com.boyue.item.entity.BySpuDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface BySpuDetailService extends IService<BySpuDetail> {

    /**
     * 查询SpuDetail接口
     *
     * @param id 商品id
     * @return SpuDetailDTO
     */
    SpuDetailDTO findSpuDetailBySpuId(Long id);
}
