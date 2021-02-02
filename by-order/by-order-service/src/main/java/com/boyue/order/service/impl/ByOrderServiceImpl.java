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
import com.boyue.order.entity.ByOrderSeckillDetail;
import com.boyue.order.enums.BusinessTypeEnum;
import com.boyue.order.enums.OrderStatusEnum;
import com.boyue.order.mapper.ByOrderMapper;
import com.boyue.order.service.ByOrderDetailService;
import com.boyue.order.service.ByOrderLogisticsService;
import com.boyue.order.service.ByOrderSeckillDetailService;
import com.boyue.order.service.ByOrderService;
import com.boyue.order.utils.PayHelper;
import com.boyue.seckill.client.SeckillClient;
import com.boyue.seckill.dto.OrderSecKillDTO;
import com.boyue.seckill.dto.SeckillPolicyDTO;
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
     * 查询超时订单业务
     * @param overDate 超时
     * @return 超时的订单集合
     */
    @Override
    public List<Long> getOverTimeIds(String overDate) {
        if (StringUtils.isEmpty(overDate)){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        return this.baseMapper.selectOverTimeIds(overDate);
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

    /**
     * 注入秒杀数据的feign接口
     */
    @Autowired
    private SeckillClient seckillClient;

    /*
     * 秒杀对象的ByOrderSeckillDetailService对象
     */
    @Autowired
    private ByOrderSeckillDetailService orderSeckillDetailService;

    /**
     * 接收秒杀订单创建消息
     *
     * @param orderSecKillDTO 秒杀订单的dto对象
     */
    @Override
    public void createSecKillOrder(OrderSecKillDTO orderSecKillDTO) {
        Long orderId = orderSecKillDTO.getOrderId();
        Long userId = orderSecKillDTO.getUserId();
        Long seckillId = orderSecKillDTO.getSeckillId();
        //远程调用seckill，获取秒杀信息
        SeckillPolicyDTO seckillPolicyDTO = seckillClient.findSecKillPolicyById(seckillId);
        //运费
        long postFee = 0;
        //实付金额
        long actualFee = seckillPolicyDTO.getCostPrice() + postFee;
        ByOrder tbOrder = new ByOrder();
        tbOrder.setOrderId(orderId);
        tbOrder.setUserId(userId);
        tbOrder.setBType(BusinessTypeEnum.SEC_KILL.value());
        tbOrder.setStatus(OrderStatusEnum.INIT.value());
        tbOrder.setActualFee(actualFee);
        tbOrder.setTotalFee(seckillPolicyDTO.getCostPrice());
        tbOrder.setPostFee(postFee);
        tbOrder.setPaymentType(orderSecKillDTO.getPaymentType());
        tbOrder.setSourceType(2);
        //保存订单表
        boolean b = save(tbOrder);
        if(!b){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        ByOrderSeckillDetail orderSeckillDetail = new ByOrderSeckillDetail();
        orderSeckillDetail.setNum(1);
        orderSeckillDetail.setOrderId(orderId);
        orderSeckillDetail.setSeckillId(seckillId);
        //保存订单详情
        boolean b1 = orderSeckillDetailService.save(orderSeckillDetail);
        if(!b1){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        //远程调用user服务，根据收货人id，查询收货人信息
        UserAddressDTO userAddressDTO = userClient.queryAddressByUser(userId,orderSecKillDTO.getAddressId());
        if(userAddressDTO == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        ByOrderLogistics tbOrderLogistics = BeanHelper.copyProperties(userAddressDTO, ByOrderLogistics.class);
        tbOrderLogistics.setOrderId(orderId);
        //保存订单物流信息表
        boolean b2 = orderLogisticsService.save(tbOrderLogistics);
        if(!b2){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        //4、减库存
        //远程调用SecKill,secKillId,num
        try{
            seckillClient.minusStock(seckillId,1);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过订单id查询订单
     *
     * @param orderId 订单id
     * @return 订单的vo对象
     */
    @Override
    public OrderVo findOrderByOrderId(Long orderId) {
        Long userId = UserHolder.getUserId();
        Integer status = OrderStatusEnum.INIT.value();
        QueryWrapper<ByOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.lambda().eq(ByOrder::getOrderId,orderId)
                .eq(ByOrder::getUserId,userId)
                .eq(ByOrder::getStatus,status);
        ByOrder order = this.getOne(orderQueryWrapper);
        if (order == null) {
            // 不存在
            throw new ByException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        // 4.封装数据
        OrderVo orderVO = BeanHelper.copyProperties(order, OrderVo.class);
        return orderVO;
    }

    /**
     * 关闭秒杀订单的listener
     */
    @Override
    public void closeOverTimeSecKillOrder() {
        //        1、查询超时订单对应的订单详情数据
        List<ByOrderSeckillDetail> seckillOrderDetailList = orderSeckillDetailService.findOvertimeSeckillOrderDetail();
        if(seckillOrderDetailList==null||seckillOrderDetailList.size()==0){
            return; //表示没有超时的订单
        }
        Map<Long, Integer> seckillIdAndNumMap = seckillOrderDetailList.stream().collect(Collectors.toMap(ByOrderSeckillDetail::getSeckillId, ByOrderSeckillDetail::getNum));
        List<Long> orderIds = seckillOrderDetailList.stream().map(ByOrderSeckillDetail::getOrderId).collect(Collectors.toList());
        //          2、更新超时订单数据
        this.getBaseMapper().cleanOvertimeSeckillOrder(orderIds);
        //        3、回复库存,别忘了还需要压栈秒杀商品库存数
        seckillClient.plusStock(seckillIdAndNumMap);
    }
}
