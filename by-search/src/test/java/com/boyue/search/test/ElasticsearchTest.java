package com.boyue.search.test;

import com.boyue.common.vo.PageResult;
import com.boyue.item.client.SpuClient;
import com.boyue.item.dto.SpuDTO;
import com.boyue.search.entity.Goods;
import com.boyue.search.repository.GoodsRepository;
import com.boyue.search.service.SearchGoodsService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/21 22:44
 * @Author: Jacky
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {
    /**
     * 自动注入feign接口
     */
    @Autowired
    private SpuClient spuClient;

    /**
     * 自动注入goods的service业务层
     */
    @Autowired
    private SearchGoodsService searchGoodsService;

    /**
     * 自动注入elasticsearch的CRUD对象
     */
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 从db中查出数据信息存储到elasticsearch中
     */
    @Test
    public void DbToEsTest(){
        //当前页码
        int page = 1;
        //每页显示条数
        int rows = 50;
        while (true){
            //查询出所有的spu对象
            PageResult<SpuDTO> pageResult = spuClient.findAllOfSpu(page, rows, null, true);
            //判断是否为空
            if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())){
                break;
            }
            //获取spu的list集合
            List<SpuDTO> spuDTOList = pageResult.getItems();

            //将spu的list集合封装为goods的list集合
            List<Goods> goodsList = new ArrayList<>();
            for (SpuDTO spuDTO : spuDTOList) {
                // 调用方法把spuDTO对象封装goods对象
                Goods goods = searchGoodsService.createGoods(spuDTO);
                goodsList.add(goods);
            }

            for (Goods goods : goodsList) {
                System.out.println(goods);
            }

            //上传到elasticSearch中
            goodsRepository.saveAll(goodsList);
            if (pageResult.getItems().size() < rows){
                break;
            }
            page++;
        }
    }
}
