package com.boyue.item.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品分类和品牌的中间表，两者是多对多关系
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ByCategoryBrand extends Model<ByCategoryBrand> {

private static final long serialVersionUID=1L;

    /**
     * 商品类目id
     */
    private Long categoryId;

    /**
     * 品牌id
     */
    private Long brandId;


    @Override
    protected Serializable pkVal() {
        return this.categoryId;
    }

}
