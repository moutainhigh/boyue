package com.boyue.common.exception;

import com.boyue.common.enums.ExceptionEnum;
import lombok.Data;
import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 23:09
 * @Author: Jacky
 * @Description: 自定义异常
 */
@Getter
public class ByException extends RuntimeException {
    /**
     * 状态码
     */
    private Integer status;

    public ByException(Integer status) {
        this.status = status;
    }

    public ByException(Integer status, String message) {
        super(message);
        this.status = status;
    }

    public ByException(Integer status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public ByException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.status = exceptionEnum.getStatus();
    }

    public ByException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(exceptionEnum.getMessage(), cause);
        this.status = exceptionEnum.getStatus();
    }
}
