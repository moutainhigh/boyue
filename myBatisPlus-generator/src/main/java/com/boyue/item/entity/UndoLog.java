package com.boyue.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
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
public class UndoLog extends Model<UndoLog> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long branchId;

    private String xid;

    private String context;

    private Blob rollbackInfo;

    private Integer logStatus;

    private Date logCreated;

    private Date logModified;

    private String ext;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
