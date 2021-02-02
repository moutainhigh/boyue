package com.boyue.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.seckill.entity.BySeckillPolicy;
import com.boyue.seckill.mapper.BySeckillPolicyMapper;
import com.boyue.seckill.service.BySeckillPolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 秒杀政策表 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-30
 */
@Service
public class BySeckillPolicyServiceImpl extends ServiceImpl<BySeckillPolicyMapper, BySeckillPolicy> implements BySeckillPolicyService {
    /**
     * 减库存
     *
     * @param seckillId 秒杀id
     * @param num       数量
     */
    @Override
    public int minusStock(Long seckillId, Integer num) {
        return this.getBaseMapper().minusStock(seckillId, num);
    }

    /**
     * 恢复秒杀库存
     * @param seckillMap
     */
    @Override
    @Transactional
    public void plusStock(Map<Long, Integer> seckillMap) {
        for (Long secKillId : seckillMap.keySet()) {
            int code = this.getBaseMapper().plusStock(secKillId,seckillMap.get(secKillId));
            if(code <1 ){
                throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
            }
        }
    }
}
