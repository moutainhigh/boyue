package com.boyue.item.controller;

import com.boyue.item.dto.SpecParamDTO;
import com.boyue.item.service.BySpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 19:31
 * @Author: Jacky
 * @Description: 规格参数SpecParam的controller层
 */
@RestController
public class SpecParamController {

    @Autowired
    private BySpecParamService specParamService;

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
    public ResponseEntity<List<SpecParamDTO>> findSpecParam(@RequestParam(name = "gid", required = false) Long gid,
                                                            @RequestParam(name = "cid", required = false) Long cid,
                                                            @RequestParam(name = "searching", required = false) Boolean searching) {
        List<SpecParamDTO> specParamDTOList = specParamService.findSpecParam(gid, cid, searching);
        return ResponseEntity.ok(specParamDTOList);
    }

    //
    //http://api.boyue.com/api/item/spec/param
}
