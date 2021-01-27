package com.boyue.common.enums;

import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 23:07
 * @Author: Jacky
 * @Description: 封装异常状态码和异常信息枚举类
 */
@Getter
public enum ExceptionEnum {
    /**
     * 错误信息的枚举
     */
    PRICE_IS_NULL(400,"价格不能为空！"),
    NAME_IS_NULL(400,"名字不能为空！"),
    INVALID_FILE_TYPE(400, "无效的文件类型！"),
    INVALID_PARAM_ERROR(400, "无效的请求参数！"),
    INVALID_PARAMETER_TYPE_ERROR(400, "无效的参数类型！"),
    INVALID_CODE_ERROR(400, "无效的验证码！"),
    UNAVAILABLE_USERNAME_ERROR(400, "不可用的用户名！"),
    UNAVAILABLE_PHONE_ERROR(400, "不可用的手机号码！"),
    INVALID_PHONE_NUMBER(400, "无效的手机号码"),
    INVALID_VERIFY_CODE(400, "验证码错误！"),
    INVALID_USERNAME_PASSWORD(400, "无效的用户名和密码！"),
    INVALID_SERVER_ID_SECRET(400, "无效的服务id和密钥！"),
    INVALID_NOTIFY_PARAM(400, "回调参数有误！"),
    INVALID_NOTIFY_SIGN(400, "收货人信息不存在！"),

    CATEGORY_NOT_FOUND(400, "商品分类不存在！"),
    USER_ADDRESS_NOT_FOUND(400, "用户地址不存在！"),
    USER_ID_NOT_FOUND(400, "用户的userId不存在！"),
    BRAND_NOT_FOUND(400, "品牌不存在！"),
    SPEC_NOT_FOUND(400, "规格不存在！"),
    SPEC_GROUP_NOT_FOUND(400, "规格参数组不存在！"),
    GOODS_NOT_FOUND(400, "商品不存在！"),
    CARTS_NOT_FOUND(400, "购物车不存在！"),
    APPLICATION_NOT_FOUND(400, "应用不存在！"),
    ORDER_NOT_FOUND(400, "订单不存在！"),
    ORDER_DETAIL_NOT_FOUND(400, "订单数据不存在！"),

    DATA_TRANSFER_ERROR(500, "数据转换异常！"),
    INSERT_OPERATION_FAIL(500, "新增操作失败！"),
    UPDATE_OPERATION_FAIL(500, "更新操作失败！"),
    DELETE_OPERATION_FAIL(500, "删除操作失败！"),
    REMOVE_PAGE_OPERATION_FAIL(500, "删除操作失败！"),
    FILE_UPLOAD_ERROR(500, "文件上传失败！"),
    DIRECTORY_WRITER_ERROR(500, "目录写入失败！"),
    FILE_WRITER_ERROR(500, "文件写入失败！"),
    SEND_MESSAGE_ERROR(500, "短信发送失败！"),
    INVALID_ORDER_STATUS(500, "订单状态不正确！"),


    FILE_FORMAT_ERROR(400, "文件格式不符合要求！"),
    USER_NOT_FOUND(400, "用户不存在，请先注册！"),
    UNAUTHORIZED(401, "登录失效或未登录！"),
    SEND_MESSAGE_history(400, "短信已发送，请不要频繁点击！"),
    GOODS_DETAIL_NOT_FOUND(400, "商品详情数据不存在！");

    ExceptionEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private Integer status;
    private String message;
}
