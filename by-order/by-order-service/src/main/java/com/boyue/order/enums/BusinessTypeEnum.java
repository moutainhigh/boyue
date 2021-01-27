package com.boyue.order.enums;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/27 23:15
 * @Author: Jacky
 * @Description: 业务类型枚举类
 */
public enum BusinessTypeEnum {
    /**
     * 业务类型
     */
    MALL(1, "商城"),
    SEC_KILL(2, "秒杀");

    /**
     * 业务类型
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;

    BusinessTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer value() {
        return this.type;
    }

    public String desc() {
        return this.desc;
    }
}
