package com.boyue.user.enums;

import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/24 22:59
 * @Author: Jacky
 * @Description: 参数类型的枚举类
 */
@Getter
public enum DataTypeEnum {
    /**
     * 参数类型枚举
     */
    USERNAME_TYPE(1),
    PHONE_TYPE(2);

    /**
     * 参数类型
     */
    private final Integer dataType;

    DataTypeEnum(Integer dataType) {
        this.dataType = dataType;
    }

}
