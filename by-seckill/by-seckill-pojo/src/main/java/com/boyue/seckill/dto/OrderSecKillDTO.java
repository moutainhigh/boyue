package com.boyue.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 15:53
 * @Author: Jacky
 * @Description: 秒杀订单dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSecKillDTO {
    /**
     * 页面上传的数据
     */
    private Long addressId;
    private Integer paymentType;
    private Long seckillId;

    /**
     * 微服务之间需要的数据
     */
    private Long userId;
    private Long orderId;
}
