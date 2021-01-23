package com.boyue.search.dto;

import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/22 16:39
 * @Author: Jacky
 * @Description: goods对象的dto对象
 */
@Data
public class GoodsDTO {
    /**
     *  spuId
     */
    private Long id;
    /**
     * 卖点
     */
    private String subTitle;
    /**
     * sku信息的json结构
     */
    private String skus;
}
