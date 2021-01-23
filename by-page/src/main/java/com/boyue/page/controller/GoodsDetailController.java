package com.boyue.page.controller;

import com.boyue.page.service.GoodsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 11:48
 * @Author: Jacky
 * @Description: 商品详细页的controller层
 */
@RestController
public class GoodsDetailController {

    @Autowired
    private GoodsDetailService goodsDetailService;

    /**
     * 显示item模板内容
     *
     * @param model 模板对象
     * @param id    商品spuId
     * @return 跳转item模板页面
     */
    @GetMapping(path = "/item/{id}.html", name = "显示item模板内容")
    public String goodsDetailPage(Model model, @PathVariable(name = "id") Long id) {
        Map<String, Object> map = goodsDetailService.loadItemData(id);
        model.addAllAttributes(map);
        return "item";
    }
}
