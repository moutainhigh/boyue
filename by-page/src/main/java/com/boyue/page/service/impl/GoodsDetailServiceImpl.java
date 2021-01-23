package com.boyue.page.service.impl;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.item.client.*;
import com.boyue.item.dto.*;
import com.boyue.page.service.GoodsDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 11:51
 * @Author: Jacky
 * @Description: 跳转item模板页面
 */
@Service
@Slf4j
public class GoodsDetailServiceImpl implements GoodsDetailService {

    /**
     * 注入spu的feignClient接口
     */
    @Autowired
    private SpuClient spuClient;

    /**
     * 注入分类category的feignClient接口
     */
    @Autowired
    private CategoryClient categoryClient;

    /**
     * 注入品牌brand的feignClient接口
     */
    @Autowired
    private BrandClient brandClient;

    /**
     * 注入商品详细参数feignClient接口
     */
    @Autowired
    private SpuDetailClient spuDetailClient;

    /**
     * 注入sku的feignClient接口
     */
    @Autowired
    private SkuClient skuClient;

    /**
     * 注入规格参数组的client接口
     */
    @Autowired
    private SpecGroupClient specGroupClient;


    /**
     * 显示item模板内容
     *
     * @param id 商品spuId
     * @return 商品数据封装到map中，返给controller
     */
    @Override
    public Map<String, Object> loadItemData(Long id) {
        //判断参数
        if (id == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //构建存储对象的map集合
        Map<String, Object> map = new HashMap<>();

        //获取spu信息
        SpuDTO spuDTO = spuClient.findSpuById(id);

        //获取商品的分类集合
        List<CategoryDTO> categoryDTOList = categoryClient.findCategoryByIds(spuDTO.getIds());

        //获取商品的品牌信息
        BrandDTO brandDTO = brandClient.findBrandListById(spuDTO.getBrandId());

        //获取商品的详细参数
        SpuDetailDTO spuDetailDTO = spuDetailClient.findSpuDetailBySpuId(id);

        //获取商品sku
        List<SkuDTO> skuDTOList = skuClient.findSkuBySpuId(id);

        //获取规格组和组内参数
        List<SpecGroupDTO> specGroupDTOList = specGroupClient.findSpecParamAndSpecGroup(spuDTO.getCid3());

        //封装map分类集合
        map.put("categories", categoryDTOList);
        //品牌信息
        map.put("brand", brandDTO);
        //spu商品信息
        map.put("spuName", spuDTO.getName());
        //spu的标题
        map.put("subTitle", spuDTO.getSubTitle());
        //商品详情参数
        map.put("detail", spuDetailDTO);
        //获取具体商品sku
        map.put("skus", skuDTOList);
        //获取specs获取规格组，组内参数
        map.put("specs", specGroupDTOList);

        return map;
    }

    @Autowired
    private TemplateEngine templateEngine;

    private String htmlPath = "D:\\idea\\nginx-1.12.2\\html\\item";

    /**
     * 生成动态模板页，页面静态化
     *
     * @param id 商品id
     */
    @Override
    public void createHtml(Long id) {
        Map<String, Object> map = this.loadItemData(id);

        //上下文
        Context context = new Context();
        //把动态获取的数据，放入上下文
        context.setVariables(map);
        //模板解析器     springboot自动配置
        //构造静态页面存放的目录,nginx目录下
        File dir = new File(htmlPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir,id+".html");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file, "UTF-8");
            //模板引擎生成静态页面
            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if(printWriter!= null){
                printWriter.close();
            }
        }
    }
}
