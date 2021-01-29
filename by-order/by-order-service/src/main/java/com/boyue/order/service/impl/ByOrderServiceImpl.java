package com.boyue.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.threadlocals.UserHolder;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.utils.IdWorker;
import com.boyue.dto.CartDTO;
import com.boyue.dto.OrderDTO;
import com.boyue.item.client.GoodsClient;
import com.boyue.item.client.SkuClient;
import com.boyue.item.dto.SkuDTO;
import com.boyue.order.entity.ByOrder;
import com.boyue.order.entity.ByOrderDetail;
import com.boyue.order.entity.ByOrderLogistics;
import com.boyue.order.enums.BusinessTypeEnum;
import com.boyue.order.enums.OrderStatusEnum;
import com.boyue.order.mapper.ByOrderMapper;
import com.boyue.order.service.ByOrderDetailService;
import com.boyue.order.service.ByOrderLogisticsService;
import com.boyue.order.service.ByOrderService;
import com.boyue.order.utils.PayHelper;
import com.boyue.user.client.UserClient;
import com.boyue.user.dto.UserAddressDTO;
import com.boyue.vo.OrderDetailVO;
import com.boyue.vo.OrderLogisticsVO;
import com.boyue.vo.OrderVo;
import com.github.wxpay.sdk.WXPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
@Slf4j
public class ByOrderServiceImpl extends ServiceImpl<ByOrderMapper, ByOrder> implements ByOrderService {

    /**
     * 注入雪花算法工具类
     */
    @Autowired
    private IdWorker idWorker;

    /**
     * 注入商品sku的feignClient接口
     */
    @Autowired
    private SkuClient skuClient;

    /**
     * 注入订单详细页的service
     */
    @Autowired
    private ByOrderDetailService orderDetailService;

    /**
     * 注入用户userAddress的feignClient接口
     */
    @Autowired
    private UserClient userClient;

    /**
     * 订单地址的service
     */
    @Autowired
    private ByOrderLogisticsService orderLogisticsService;

    /**
     * 注入goods商品的feignClient接口
     */
    @Autowired
    private GoodsClient goodsClient;

    /**
     * 注入微信支付的工具类
     */
    @Autowired
    private PayHelper payHelper;

    /**
     * 用户登陆后，提交购物车数据，创建订单
     *
     * @param orderDTO 订单数据
     * @return 订单id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long creatOrder(OrderDTO orderDTO) {
        //校验参数
        if (orderDTO == null){
            log.error("无效的请求参数");
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //1、保存order订单
        //使用雪花算法生成订单id
        long orderId = idWorker.nextId();
        //获取用户id
        Long userId = UserHolder.getUserId();
        //获得order中的skuId
        List<Long> skuIds = orderDTO.getCarts().stream().map(CartDTO::getSkuId).collect(Collectors.toList());
        //获取sku集合数据
        List<SkuDTO> skuDTOList = skuClient.findSkuByListIds(skuIds);

        //获取订单商品的id和数量的键值对
        //键  skuId  值   num
        Map<Long, Integer> map = orderDTO.getCarts().stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));

        //初始化总金额
        long totalFee = 0;

        //定义list集合手机订单商品详细参数
        List<ByOrderDetail> orderDetailList = new ArrayList<>();

        //2、保存订单详细数据
        //计算总金额
        for (SkuDTO skuDTO : skuDTOList) {
            //获取skuId
            Long skuId = skuDTO.getId();
            //获取价格
            Long price = skuDTO.getPrice();
            //通过skuId获取订单商品的数量
            Integer number = map.get(skuId);

            //总金额累加
            totalFee += (price * number);

            //手机orderDetail数据
            ByOrderDetail orderDetail = new ByOrderDetail();
            //设置订单号
            orderDetail.setOrderId(orderId);
            //设置商品skuId
            orderDetail.setSkuId(skuId);
            //设置数量
            orderDetail.setNum(number);
            //设置商品title
            orderDetail.setTitle(skuDTO.getTitle());
            //设置商品动态属性键值集
            orderDetail.setOwnSpec(skuDTO.getOwnSpec());
            //设置价格
            orderDetail.setPrice(price);
            //设置图片
            orderDetail.setImage(StringUtils.split(skuDTO.getImages(), ",")[0]);
            //添加list集合中
            orderDetailList.add(orderDetail);
        }

        //计算运费,默认包邮
        long postFee = 0;

        //计算实付金额
        long actualFee = postFee + totalFee;

        //构建order对象
        ByOrder order = new ByOrder();
        //订单id
        order.setOrderId(orderId);
        //总金额
        order.setTotalFee(totalFee);
        //实付金额
        order.setActualFee(actualFee);
        //支付方式
        order.setPaymentType(orderDTO.getPaymentType());
        //订单业务类型
        order.setBType(BusinessTypeEnum.MALL.value());
        //邮费
        order.setPostFee(postFee);
        //用户id
        order.setUserId(userId);
        //订单来源
        order.setSourceType(2);
        //状态
        order.setStatus(OrderStatusEnum.INIT.value());

        //存储到数据库
        boolean saveFlag = this.save(order);
        if (!saveFlag) {
            log.error("order保存失败");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //保存订单详细数据
        boolean orderDetailFlag = orderDetailService.saveBatch(orderDetailList);
        if (!orderDetailFlag) {
            log.error("orderDetailFlag保存失败");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //3、保存物流数据
        //获取用户地址
        UserAddressDTO userAddressDTO = userClient.findUserAddressById(orderDTO.getAddressId());
        log.info("userAddressDTO={}", userAddressDTO);
        if (userAddressDTO == null) {
            log.error("userAddressDTO为空");
            throw new ByException(ExceptionEnum.USER_ADDRESS_NOT_FOUND);
        }


        //类型转换
        ByOrderLogistics orderLogistics = BeanHelper.copyProperties(userAddressDTO, ByOrderLogistics.class);
        //设置订单id
        orderLogistics.setOrderId(orderId);

        boolean logisticFlag = orderLogisticsService.save(orderLogistics);
        if (!logisticFlag) {
            log.error("orderLogistics保存失败");
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //4、远程调度，减库存
        log.info("map={}",map);
        try {
            goodsClient.minusStock(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("减库存失败");
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        return orderId;
    }

    /**
     * 根据订单id查询订单信息
     *
     * @param orderId 订单id
     * @return 订单的vo对象
     */
    @Override
    public OrderVo findOrderById(Long orderId) {
        //获取用户id
        Long userId = UserHolder.getUserId();
        //订单状态初始化
        Integer status = OrderStatusEnum.INIT.value();
        //查询订单
        ByOrder order = this.findOrder(orderId, userId, status);
        if (order == null){
            //订单不存在
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        if (!userId.equals(order.getUserId())){
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        orderId = order.getOrderId();


        //查询订单详细
        QueryWrapper<ByOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ByOrderDetail::getOrderId,orderId);
        List<ByOrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        // 3.查询订单状态
        QueryWrapper<ByOrderLogistics> orderLogisticsQueryWrapper = new QueryWrapper<>();
        orderLogisticsQueryWrapper.lambda().eq(ByOrderLogistics::getOrderId,orderId);
        ByOrderLogistics orderLogistics = orderLogisticsService.getOne(orderLogisticsQueryWrapper);
//        ByOrderLogistics orderLogistics = orderLogisticsService.getById(orderId);
        if (orderLogistics == null) {
            // 不存在
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        // 4.封装数据
        OrderVo orderVo = BeanHelper.copyProperties(order, OrderVo.class);

        orderVo.setDetailList(BeanHelper.copyWithCollection(orderDetailList, OrderDetailVO.class));
        orderVo.setLogistics(BeanHelper.copyProperties(orderLogistics, OrderLogisticsVO.class));
        return orderVo;
    }

    /**
     * 查询订单信息
     *
     * @param orderId 订单id
     * @param userId 用户id
     * @param status 订单状态
     * @return 查询出的订单对象
     */
    @Override
    public ByOrder findOrder(Long orderId, Long userId, Integer status) {
        QueryWrapper<ByOrder> queryWrapper = new QueryWrapper<>();
        if(orderId != null){
            queryWrapper.lambda().eq(ByOrder::getOrderId,orderId);
        }
        queryWrapper.lambda().eq(ByOrder::getUserId,userId).
                eq(ByOrder::getStatus,status).
                orderByDesc(ByOrder::getCreateTime);
        ByOrder order = this.getOne(queryWrapper);
        if(order == null){
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        return order;
    }

    /**
     * 修改订单状态
     *
     * @param map 响应的商品信息
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //校验参数 验证微信支付结果通知
        if(map.get("result_code") == null || !map.get("result_code").equals(WXPayConstants.SUCCESS)){
            throw new ByException(ExceptionEnum.INVALID_NOTIFY_PARAM);
        }
        try {
            payHelper.isValidSign(map);
        } catch (Exception e) {
            throw new ByException(ExceptionEnum.INVALID_NOTIFY_PARAM);
        }
        //获取订单号
        String outTradeNo = map.get("out_trade_no");
        Long orderId = Long.valueOf(outTradeNo);
        //通过id获取订单信息
        ByOrder order = this.getById(orderId);
        if(order == null){
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        //获取实付金额
        Long actualFee = order.getActualFee();
        //获取用户的支付金额
        String totalFeeStr = map.get("total_fee");
        Long totalFee = Long.valueOf(totalFeeStr);

        //TODO 用实付金额和用户支付金额比较
        /*if(actualFee.longValue() != totalFee.longValue()){
            //实付金额和用户支付金额不一致
            throw new ByException(ExceptionEnum.INVALID_NOTIFY_PARAM);
        }*/
        //修改状态,一定要幂等 ，把订单状态从1 改成 2
        UpdateWrapper<ByOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(ByOrder::getStatus,OrderStatusEnum.PAY_UP.value())
                .eq(ByOrder::getOrderId,orderId)
                .eq(ByOrder::getStatus,OrderStatusEnum.INIT.value());

        boolean updateFlag = this.update(updateWrapper);
        if(!updateFlag){
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

    }

    /**
     * 关闭 超时未支付的订单
     *
     * @param time 当前时间-15分钟
     */
    @Override
    public void closeOverTimeOrder(String time) {
        //校验参数
        if (StringUtils.isEmpty(time)){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //查询小于time并且没有支付的订单号
        List<Long> orderIds  = this.getBaseMapper().selectOverTimeOrderId(time);
        if(CollectionUtils.isEmpty(orderIds)){
            return;
        }
        //构造更新条件
        UpdateWrapper<ByOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ByOrder::getStatus,OrderStatusEnum.INIT.value()).
                in(ByOrder::getOrderId,orderIds).
                set(ByOrder::getStatus,OrderStatusEnum.CLOSED.value());
        //关闭订单
        boolean updateFlag = update(updateWrapper);
        if(!updateFlag){
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        //构造查询条件\
        QueryWrapper<ByOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ByOrderDetail::getOrderId,orderIds);
        //查询订单对应的商品数量，查询tb_order_detail
        List<ByOrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        //构造恢复库存对象集合
        Map<Long, Integer> skuIdNumMap = orderDetailList.stream().
                collect(Collectors.groupingBy(ByOrderDetail::getSkuId, Collectors.summingInt(ByOrderDetail::getNum)));
        //恢复库存
        try{
            goodsClient.plusStock(skuIdNumMap);
        }catch (Exception e){
            e.printStackTrace();
            log.info("恢复库存失败，orderIds={}",orderIds);
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }
}
