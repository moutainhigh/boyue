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
 * 服务信息表，记录微服务的id，名称，密文，用来做服务认证
 * </p>
 *
 * @author Jacky
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ByApplication extends Model<ByApplication> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 服务介绍
     */
    private String info;

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
