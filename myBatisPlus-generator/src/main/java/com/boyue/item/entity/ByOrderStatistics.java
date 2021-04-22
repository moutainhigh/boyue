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
 * 
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ByOrderStatistics extends Model<ByOrderStatistics> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 订单数量
     */
    private Long quantity;

    /**
     * 总金额
     */
    private Long sum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
