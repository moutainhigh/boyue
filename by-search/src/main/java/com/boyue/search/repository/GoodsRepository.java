package com.boyue.search.repository;

import com.boyue.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/21 17:14
 * @Author: Jacky
 * @Description:  提供elasticsearch的CRUD方法
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
