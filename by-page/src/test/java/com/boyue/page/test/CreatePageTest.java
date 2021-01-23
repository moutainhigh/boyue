package com.boyue.page.test;

import com.boyue.common.vo.PageResult;
import com.boyue.item.client.SpuClient;
import com.boyue.item.dto.SpuDTO;
import com.boyue.page.service.GoodsDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/23 14:28
 * @Author: Jacky
 * @Description: 生成商品静态页
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CreatePageTest {

    @Autowired
    private GoodsDetailService goodsDetailService;

    @Autowired
    private SpuClient spuClient;

    @Test
    public void test01(){
        int page =1;
        int rows=30;

        while (true){
            PageResult<SpuDTO> pageResult = spuClient.findAllOfSpu(page, rows, null, true);
            if(pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())){
                break;
            }
            List<SpuDTO> list = pageResult.getItems();
            for (SpuDTO spuDTO : list) {
                goodsDetailService.createHtml(spuDTO.getId());
            }
            if(list.size() < rows){
                break;
            }
            page ++;
        }
    }

    @Test
    public void test02() {
        Map<String, Object> map = goodsDetailService.loadItemData(2L);
        for (String key : map.keySet()) {
            Object value = map.get(key);
            System.out.println(key+"--------"+value);
        }
    }

    @Test
    public void test03() {
        goodsDetailService.createHtml(2L);
    }
}
