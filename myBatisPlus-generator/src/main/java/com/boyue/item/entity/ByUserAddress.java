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
 * 用户收货地址表
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ByUserAddress extends Model<ByUserAddress> {

private static final long serialVersionUID=1L;

    /**
     * 地址id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人名称
     */
    private String addressee;

    /**
     * 收货人电话
     */
    private String phone;

    /**
     * 收货人省份
     */
    private String province;

    /**
     * 收货人市
     */
    private String city;

    /**
     * 收货人区
     */
    private String district;

    /**
     * 收货人详细地址
     */
    private String address;

    /**
     * 收货人邮编
     */
    private String postcode;

    /**
     * 是否默认 0-不是  1-是
     */
    private Integer isDefault;

    private Date createTime;

    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
