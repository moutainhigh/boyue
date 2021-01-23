package com.boyue.item.client;

import com.boyue.item.dto.SpecGroupDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/21 15:00
 * @Author: Jacky
 * @Description: 规格参数组的client接口
 */
@FeignClient(value = "item-service")
public interface SpecGroupClient {
    /**
     * 获取规格组
     * 请求路径  GET /spec/groups/of/category?id=76
     *
     * @param id 分类id
     * @return 规格组的list集合
     */
    @GetMapping(path = "/spec/groups/of/category", name = "获取规格组")
    List<SpecGroupDTO> findSpecGroupById(@RequestParam(name = "id") Long id);

    /**
     * 通过分类id查询商品的规格组和组内参数
     *
     * @param id 分类id
     * @return specGroupDTO的list集合
     */
    @GetMapping(path = "/{id}", name = "通过分类id查询商品的规格组和组内参数")
    List<SpecGroupDTO> findSpecParamAndSpecGroup(@PathVariable(name = "id") Long id);
}
