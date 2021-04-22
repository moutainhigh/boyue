package com.boyue.item.service.impl;

import com.boyue.item.entity.ByCategory;
import com.boyue.item.mapper.ByCategoryMapper;
import com.boyue.item.service.ByCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Service
public class ByCategoryServiceImpl extends ServiceImpl<ByCategoryMapper, ByCategory> implements ByCategoryService {

}
