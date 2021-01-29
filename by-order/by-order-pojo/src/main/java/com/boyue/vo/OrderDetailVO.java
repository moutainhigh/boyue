package com.boyue.vo;

import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/29 14:05
 * @Author: Jacky
 * @Description: OrderDetail的vo对象
 */
@Data
public class OrderDetailVO {
    private Long id;
    /**
     * 订单编号
     */
    private Long orderId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 商品购买数量
     */
    private Integer num;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品单价
     */
    private Long price;
    /**
     * 商品规格数据
     */
    private String ownSpec;
    /**
     * 图片
     */
    private String image;
}
