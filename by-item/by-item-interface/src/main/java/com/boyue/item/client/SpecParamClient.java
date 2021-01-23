package com.boyue.item.client;

import com.boyue.item.dto.SpecParamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 19:31
 * @Author: Jacky
 * @Description: 规格参数SpecParam的client层
 */
@FeignClient(value = "item-service")
public interface SpecParamClient {
    /**
     * 获取规格参数
     * GET /spec/params?gid=1&cid=2
     *
     * @param gid       规格组id
     * @param cid       分类id
     * @param searching 是否搜索
     * @return 规格组的list集合
     */
    @GetMapping(path = "/spec/params",name = "获取规格参数")
    List<SpecParamDTO> findSpecParam(@RequestParam(name = "gid", required = false) Long gid,
                                     @RequestParam(name = "cid", required = false) Long cid,
                                     @RequestParam(name = "searching", required = false) Boolean searching);
}
