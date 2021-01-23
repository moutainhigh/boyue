package com.boyue.item.client;

import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.SpuDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/19 20:11
 * @Author: Jacky
 * @Description: spu的client接口层
 */
@FeignClient(value = "item-service")
public interface SpuClient {

    /**
     * 查询商品SPU信息 ，分页查询
     * GET /spu/page
     *
     * @param page     当前页
     * @param rows     每页显示条数
     * @param key      过滤条件
     * @param saleable 上架或下架
     * @return pageResult对象
     */
    @GetMapping(path = "/spu/page", name = "查询商品SPU信息 ，分页查询")
    PageResult<SpuDTO> findAllOfSpu(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(name = "rows", required = false, defaultValue = "5") Integer rows,
                                    @RequestParam(name = "key", required = false) String key,
                                    @RequestParam(name = "saleable", required = false) Boolean saleable);

    /**
     * 根据主键id查询sou信息
     *
     * @param id 主键id
     * @return spuDTO对象
     */
    @GetMapping(path = "/spu/{id}", name = "根据主键id查询spu信息")
    SpuDTO findSpuById(@PathVariable(name = "id") Long id);
}
