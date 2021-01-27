package com.boyue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 23:18
 * @Author: Jacky
 * @Description: 再次封装购物车数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    /**
     * 商品sku的id
     */
    private Long skuId;

    /**
     * 商品购买的数量
     */
    private Integer num;
}
