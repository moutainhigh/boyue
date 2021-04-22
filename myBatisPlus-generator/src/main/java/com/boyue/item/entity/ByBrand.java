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
 * 品牌表，一个品牌下有多个商品（spu），一对多关系
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ByBrand extends Model<ByBrand> {

private static final long serialVersionUID=1L;

    /**
     * 品牌id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌图片地址
     */
    private String image;

    /**
     * 品牌的首字母
     */
    private String letter;

    private Date createTime;

    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
