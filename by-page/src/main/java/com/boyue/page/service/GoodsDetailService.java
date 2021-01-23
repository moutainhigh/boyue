package com.boyue.page.service;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 11:51
 * @Author: Jacky
 * @Description: 商品详细页的service接口
 */
public interface GoodsDetailService {
    /**
     * 显示item模板内容
     *
     * @param id 商品spuId
     * @return 商品数据封装到map中，返给controller
     */
    Map<String, Object> loadItemData(Long id);

    /**
     * 生成动态模板页
     * @param id 商品id
     */
    void createHtml(Long id);
}