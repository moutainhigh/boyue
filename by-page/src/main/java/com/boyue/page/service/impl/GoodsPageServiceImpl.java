package com.boyue.page.service.impl;

import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.item.client.*;
import com.boyue.item.dto.*;
import com.boyue.page.service.GoodsPageService;
import com.boyue.seckill.client.SeckillClient;
import com.boyue.seckill.dto.SeckillPolicyDTO;
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
public class  GoodsPageServiceImpl implements GoodsPageService {

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

    /**
     * 指定模板路径
     */
    private final String htmlPath = "D:\\idea\\nginx-1.12.2\\html";

    /**
     * 存放商品详细页的目录路径
     */
    private final String itemHtmlPath = htmlPath + "\\item";

    /**
     * 存放秒杀商品的详细页的目录路径
     */
    private final String seckillHtmlPath = htmlPath + "\\seckill";

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
        File dir = new File(itemHtmlPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, id + ".html");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file, "UTF-8");
            //模板引擎生成静态页面
            templateEngine.process("item", context, printWriter);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * 删除动态模板页
     *
     * @param id 商品id
     */
    @Override
    public void removeHtml(Long id) {
        if (id == null) {
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        File dir = new File(itemHtmlPath);
        if (!dir.exists()) {
            return;
        }
        File file = new File(dir, id + ".html");
        boolean flag = file.delete();
        if (!flag) {
            throw new ByException(ExceptionEnum.REMOVE_PAGE_OPERATION_FAIL);
        }
    }

    /**
     * 注入秒杀商品的feignClient接口
     */
    @Autowired
    private SeckillClient seckillClient;

    /**
     * 创建秒杀的静态页面
     * 包含列表页 和 详情页
     *
     * @param date 秒杀日期
     */
    @Override
    public void createSecKillPage(String date) {

        //根据秒杀日期，获取对应的秒杀列表数据
        List<SeckillPolicyDTO> secKillPolicyList = seckillClient.findSecKillPolicyList(date);

        //创建列表页，seckillList
        createSecKillListPage(secKillPolicyList);

        for (SeckillPolicyDTO seckillPolicyDTO : secKillPolicyList) {
            //创建详情页
            createSecKillDetailPage(seckillPolicyDTO);
        }
    }

    /**
     * 创建详情页面
     *
     * @param seckillPolicyDTO 秒杀商品的dto对象
     */
    private void createSecKillDetailPage(SeckillPolicyDTO seckillPolicyDTO) {

        //获取模板页面需要的动态数据
        //远程调用item服务，根据spuid，获取detail数据
        SpuDetailDTO spuDetailDTO = spuDetailClient.findSpuDetailBySpuId(seckillPolicyDTO.getSpuId());
        //远程调用item服务，根据分类id查询规格属性组和 组内属性
        List<SpecGroupDTO> specGroupDTOList = specGroupClient.findSpecParamAndSpecGroup(seckillPolicyDTO.getCid3());

        Map<String, Object> map = new HashMap<>();
        map.put("detail", spuDetailDTO);
        map.put("seckillgoods", seckillPolicyDTO);
        //规格数据组和名字
        map.put("specs", specGroupDTOList);

        Context context = new Context();
        context.setVariables(map);
        //构造页面存储路径
        File dir = new File(seckillHtmlPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        //构造列表页面file对象
        File listPage = new File(dir, seckillPolicyDTO.getId() + ".html");
        //构造writer对象
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(listPage, "UTF-8");
            //使用模板引擎生成页面
            templateEngine.process("seckill-item", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 创建列表页
     * 把静态页面放入nginx html的 /seckill/list.html
     *
     * @param secKillPolicyList 秒杀商品的list集合
     */
    private void createSecKillListPage(List<SeckillPolicyDTO> secKillPolicyList) {

        //创建上下文
        Context context = new Context();
        //设置数据
        context.setVariable("seckillList", secKillPolicyList);

        //构造页面存储路径
        File dir = new File(seckillHtmlPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        //构造列表页面file对象
        File listPage = new File(dir, "list.html");
        //构造writer对象
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(listPage, "UTF-8");
            //使用模板引擎生成页面
            templateEngine.process("seckill-index", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
