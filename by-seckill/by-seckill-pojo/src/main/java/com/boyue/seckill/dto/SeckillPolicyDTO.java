package com.boyue.seckill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 15:42
 * @Author: Jacky
 * @Description: 秒杀政策表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeckillPolicyDTO {
    /**
     * 秒杀id
     */
    private Long id;
    /**
     * spu id
     */
    private Long spuId;

    /**
     * spu名称
     */
    private String name;

    /**
     * 1级类目id
     */
    private Long cid1;

    /**
     * 2级类目id
     */
    private Long cid2;

    /**
     * 3级类目id
     */
    private Long cid3;

    /**
     * 商品所属品牌id
     */
    private Long brandId;

    /**
     * sku ID
     */
    private Long skuId;

    /**
     * 标题
     */
    private String title;

    /**
     * 标题
     */
    private String subTitle;

    /**
     * 秒杀商品数
     */
    private Integer num;

    /**
     * 剩余库存数
     */
    private Integer stockCount;

    /**
     * 商品图片
     */
    private String skuPic;

    /**
     * 原价格
     */
    private Long oldPrice;

    /**
     * 秒杀价格
     */
    private Long costPrice;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 秒杀日期
     */
    private String secKillDate;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 获取分类id的集合
     * @return 分类id的list集合
     */
    @JsonIgnore
    public List<Long> getCategorys() {
        return Arrays.asList(cid1, cid2, cid3);
    }
}
