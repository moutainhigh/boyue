package com.boyue.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/26 21:52
 * @Author: Jacky
 * @Description: 购物车的DTO对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品的图片
     */
    private String image;

    /**
     * 加入购物车时的价格
     */
    private Long price;

    /**
     * 购买商品的数量
     */
    private Integer num;

    /**
     * 商品规格参数
     */
    private String ownSpec;
}
