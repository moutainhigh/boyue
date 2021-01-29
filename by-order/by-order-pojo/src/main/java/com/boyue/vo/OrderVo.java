package com.boyue.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 23:20
 * @Author: Jacky
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderVo {


    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 总金额，单位为分
     */
    private Long totalFee;

    /**
     * 实付金额。单位:分。如:20007，表示:200元7分
     */
    private Long actualFee;

    /**
     * 优惠活动id，多个以，隔开
     */
    private String promotionIds;

    /**
     * 支付类型，1、微信支付，2、货到付款
     */
    private Integer paymentType;

    /**
     * 订单业务类型1- 商城订单 2、秒杀订单
     */
    private Integer bType;

    /**
     * 邮费。单位:分。如:20007，表示:200元7分
     */
    private Long postFee;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 发票类型(0无发票1普通发票，2电子发票，3增值税发票)
     */
    private Integer invoiceType;

    /**
     * 订单来源：1:app端，2：pc端，3：微信端
     */
    private Integer sourceType;

    /**
     * 订单的状态，1、未付款 2、已付款,未发货 3、已发货,未确认 4、确认收货，交易成功 5、交易取消，订单关闭 6、交易结束，已评价
     */
    private Integer status;

    /**
     * 订单物流信息
     */
    private OrderLogisticsVO logistics;
    /**
     * 订单详情信息
     */
    private List<OrderDetailVO> detailList;
}
