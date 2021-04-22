package com.boyue.item.service;

import com.boyue.item.entity.ByCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
public interface ByCategoryService extends IService<ByCategory> {

}
