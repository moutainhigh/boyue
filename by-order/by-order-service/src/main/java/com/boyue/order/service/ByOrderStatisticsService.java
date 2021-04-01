package com.boyue.order.service;

import com.boyue.common.vo.PageResult;
import com.boyue.dto.OrderStatisticsDTO;
import com.boyue.order.entity.ByOrder;
import com.boyue.order.entity.ByOrderStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-03-25
 */
public interface ByOrderStatisticsService extends IService<ByOrderStatistics> {

    /**
     * 功能说明：获取订单每月的销售情况
     *
     * @param page   当前页码
     * @param rows   每页显示条数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @return 返回当年每月的销售订单的数量和销售总额
     */
    PageResult<OrderStatisticsDTO> statistics(Integer year, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 将订单信息添加到orderStatistics中
     *
     * @param order 订单对象
     * @return 成功或失败
     */
    boolean addOrderStatistics(ByOrder order);
}
