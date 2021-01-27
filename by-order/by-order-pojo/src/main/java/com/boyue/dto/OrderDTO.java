package com.boyue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 23:18
 * @Author: Jacky
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    /**
     * 收货人信息id
     */
    private Long addressId;

    /**
     * 支付方式
     */
    private Integer paymentType;

    /**
     * 购物清单
     */
    private List<CartDTO> carts;
}
