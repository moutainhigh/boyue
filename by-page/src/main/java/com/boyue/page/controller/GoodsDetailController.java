package com.boyue.page.controller;

import com.boyue.page.service.GoodsPageService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
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
@Api("商品详细页服务中心GoodsDetailController")
@Slf4j
public class GoodsDetailController {

    /**
     * 注入goodsPageService
     */
    @Autowired
    private GoodsPageService goodsPageService;

    /**
     * 显示item模板内容
     *
     * @param model 模板对象
     * @param id    商品spuId
     * @return 跳转item模板页面
     */
    @ApiOperation(value = "显示item模板内容")
    @GetMapping(path = "/item/{id}.html", name = "显示item模板内容")
    public String goodsDetailPage(Model model, @PathVariable(name = "id") Long id) {
        log.info("[by-page服务]goodsDetailPage接口接收到请求,正在完成显示item模板内容");
        Map<String, Object> map = goodsPageService.loadItemData(id);
        model.addAllAttributes(map);
        return "item";
    }
}
