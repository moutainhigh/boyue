package com.boyue.vo;

import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 14:05
 * @Author: Jacky
 * @Description: OrderLogistics的vo对象
 */
@Data
public class OrderLogisticsVO {
    private Long orderId;
    /**
     * 物流单号
     */
    private String logisticsNumber;
    /**
     * 物流名称
     */
    private String logisticsCompany;
    /**
     * 收件人
     */
    private String addressee;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 街道
     */
    private String street;
    /**
     * 邮编
     */
    private String postcode;
}
