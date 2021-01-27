package com.boyue.order.enums;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 23:16
 * @Author: Jacky
 * @Description: 订单状态枚举
 */
public enum OrderStatusEnum {
    /**
     * 订单状态状态
     */
    INIT(1, "初始化，未付款"),
    PAY_UP(2, "已付款，未发货"),
    DELIVERED(3, "已发货，未确认"),
    CONFIRMED(4, "已确认,未评价"),
    CLOSED(5, "已关闭"),
    RATED(6, "已评价，交易结束");

    private final Integer value;
    private final String msg;

    OrderStatusEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public Integer value() {
        return this.value;
    }

    public String msg() {
        return msg;
    }
}
