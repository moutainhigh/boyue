package com.boyue.common.vo;

import com.boyue.common.exception.ByException;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 23:12
 * @Author: Jacky
 * @Description: 自定义异常结果
 */
@Data
public class ExceptionResult {
    private Integer status;
    private String message;
    private String timestamp;

    public ExceptionResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ExceptionResult(ByException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }
}
