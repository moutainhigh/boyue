package com.boyue.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.boyue.common.utils.constants.RegexPatterns;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ByUser extends Model<ByUser> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @Length(min = 4,max = 12,message = "用户名为4~12位字母、数字、下划线")
    private String username;

    /**
     * 密码，加密存储
     */
    @Length(min = 6,max = 18,message = "密码位6~18位字母、数字、下划线")
    private String password;

    /**
     * 注册手机号
     */
    @Pattern(regexp = RegexPatterns.PHONE_REGEX,message = "手机号格式不正确")
    private String phone;

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
