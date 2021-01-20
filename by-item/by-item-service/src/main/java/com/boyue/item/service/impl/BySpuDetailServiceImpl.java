package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.item.dto.SpuDetailDTO;
import com.boyue.item.entity.BySpuDetail;
import com.boyue.item.mapper.BySpuDetailMapper;
import com.boyue.item.service.BySpuDetailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class BySpuDetailServiceImpl extends ServiceImpl<BySpuDetailMapper, BySpuDetail> implements BySpuDetailService {

    /**
     * 根据spuId查询spuDetail商品详细参数
     *
     * @param id 商品id
     * @return SpuDetailDTO
     */
    @Override
    public SpuDetailDTO findSpuDetailBySpuId(Long id) {
        if (id == null) {
            log.error("******** 传递参数id为空 **********");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        BySpuDetail spuDetail = this.getById(id);
        if (spuDetail == null) {
            log.error("******** 查询结果为空  **********");
            throw new ByException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return BeanHelper.copyProperties(spuDetail, SpuDetailDTO.class);
    }
}
