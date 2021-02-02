package com.boyue.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;
import java.util.Set;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/21 14:39
 * @Author: Jacky
 * @Description: 每一个goods对象对应es索引库一条文档数据
 */
@Data
@Document(indexName = "boyue-goods", type = "docs", replicas = 1, shards = 3)
//@Document(indexName = "boyue", type = "docs", replicas = 1, shards = 3)
public class Goods {
    /**
     * spuId 唯一标识id，是字符串类型
     */
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;
    /**
     * 包含所有需要关键词检索的内容  name , brandName ,categoryName， 分词text ，选择分词器ik_max_word
     * 用来进行全文检索的字段，里面包含标题、商品分类、品牌、规格等信息
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;
    /**
     * [
     *      {skuid:1,image:xx.jpg,price:1000,title:xxxx},
     *      {skuid:1,image:xx.jpg,price:1000,title:xxxx}
     * ]，keyword 不分词，index=false
     * 用于页面展示的sku信息，因为不参与搜索，所以转为json存储。然后设置不索引，不搜索。包含skuId、image、price、title字段
     */
    @Field(type = FieldType.Keyword, index = false)
    private String skus;
    /**
     * 促销信息 ，keyword，index=false
     */
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * Set[1000,2000]
     * 价格数组，是所有sku的价格集合。方便根据价格进行筛选过滤
     */
    private Set<Long> price;
    /**
     * 存毫秒值
     */
    private Long createTime;
    /**
     * {key:val,key1:val1}   规格参数名字和值的对应,es会特殊处理
     * 会生成新的字段
     * 处理成  specs.key = val    specs.key1 =val1
     * 所有规格参数的集合。key是参数名，值是参数值。
     * {
     *     "specs":{
     *         "内存":[4G,6G],
     *         "颜色":"红色"
     *     }
     * }
     */
    private Map<String, Object> specs;
}
