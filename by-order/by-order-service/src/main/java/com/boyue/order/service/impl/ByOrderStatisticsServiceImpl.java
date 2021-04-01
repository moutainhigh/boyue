package com.boyue.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.vo.PageResult;
import com.boyue.dto.OrderStatisticsDTO;
import com.boyue.order.entity.ByOrder;
import com.boyue.order.entity.ByOrderStatistics;
import com.boyue.order.mapper.ByOrderStatisticsMapper;
import com.boyue.order.service.ByOrderStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-03-25
 */
@Service
@Slf4j
public class ByOrderStatisticsServiceImpl extends ServiceImpl<ByOrderStatisticsMapper, ByOrderStatistics> implements ByOrderStatisticsService {

    /**
     * 功能说明：获取订单每月的销售情况
     *
     * @param page   当前页码
     * @param rows   每页显示条数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @return 返回当年每月的销售订单的数量和销售总额
     */
    @Override
    public PageResult<OrderStatisticsDTO> statistics(Integer year, Integer page, Integer rows, String sortBy, Boolean desc) {

        //构造分页查询条件
        IPage<ByOrderStatistics> iPage = new Page<>(page, rows);
        //构造查询条件
        QueryWrapper<ByOrderStatistics> queryWrapper = new QueryWrapper<>();

        //设置查询条件
        //设置年份
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String currentYear = simpleDateFormat.format(currentTime);
        queryWrapper.lambda().eq(ByOrderStatistics::getYear,currentYear);
        if (StringUtils.isNotBlank(sortBy)) {
            if (desc) {
                //倒叙
                queryWrapper.orderByDesc(sortBy);
            } else {
                //正序
                queryWrapper.orderByAsc(sortBy);
            }
        }
        //查询
        IPage<ByOrderStatistics> orderStatisticsIPage = this.page(iPage, queryWrapper);
        //获取查询结果集
        List<ByOrderStatistics> orderStatisticsList = orderStatisticsIPage.getRecords();
        log.info("查询出的订单统计数据为orderStatisticsList={}",orderStatisticsList);
        if (CollectionUtils.isEmpty(orderStatisticsList)) {
            log.error("订单统计数据不存在");
            throw new ByException(ExceptionEnum.ORDER_STATISTICS_NOT_FOUND);
        }



        //类型转换
        List<OrderStatisticsDTO> orderStatisticsDTOList = BeanHelper.copyWithCollection(orderStatisticsList, OrderStatisticsDTO.class);

        if (CollectionUtils.isEmpty(orderStatisticsDTOList)){
            log.error("数据类型转换失败");
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        //获取总条数
        long total = orderStatisticsIPage.getTotal();
        //获取总页数
        long totalPage = orderStatisticsIPage.getPages();

        //封装为pageResult
        PageResult<OrderStatisticsDTO> pageResult = new PageResult<>();
        //设置查询结果的list集合
        pageResult.setItems(orderStatisticsDTOList);
        //设置总页数
        pageResult.setTotalPage(totalPage);
        //设置总条数
        pageResult.setTotal(total);

        return pageResult;
    }

    /**
     * 将订单信息添加到orderStatistics中
     *
     * @param order 订单对象
     * @return 成功或失败
     */
    @Override
    public boolean addOrderStatistics(ByOrder order) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String createYear = simpleDateFormat.format(order.getCreateTime());

        //去除数据库中的数据
        QueryWrapper<ByOrderStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ByOrderStatistics::getYear,createYear);
        ByOrderStatistics orderStatistics = this.getOne(queryWrapper);

        if (orderStatistics == null){
            throw new ByException(ExceptionEnum.ORDER_STATISTICS_NOT_FOUND);
        }

        //获取订单总数
        Long quantity = orderStatistics.getQuantity() + 1;
        //获取销售总金额
        Long sum = orderStatistics.getSum() + order.getActualFee();

        //修改数据
        orderStatistics.setQuantity(quantity);
        orderStatistics.setSum(sum);

        //修改数据库的数据信息
        boolean flag = this.updateById(orderStatistics);


        return flag;
    }
}
