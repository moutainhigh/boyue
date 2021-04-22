package com.boyue.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 秒杀政策表
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BySeckillPolicy extends Model<BySeckillPolicy> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * spu id
     */
    private Long spuId;

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
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime;

    /**
     * spu名称
     */
    private String name;

    /**
     * 秒杀日期
     */
    private String secKillDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
