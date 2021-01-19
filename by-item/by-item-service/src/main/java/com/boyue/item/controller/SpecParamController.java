package com.boyue.item.controller;

import com.boyue.item.dto.SpecParamDTO;
import com.boyue.item.service.BySpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 新增规格参数
     * 请求路径  POST /spec/param
     *
     * @param specParamDTO 新增对象
     * @return 空
     */
    @PostMapping(path = "/param", name = "新增规格参数")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParamDTO specParamDTO) {
        specParamService.saveSpecParam(specParamDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改规格参数
     * 请求路径  PUT /spec/param
     *
     * @param specParamDTO 新增对象
     * @return 空
     */
    @PutMapping(path = "/param", name = "修改规格参数")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParamDTO specParamDTO) {
        specParamService.updateSpecParam(specParamDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除规格参数
     * 请求路径 DELETE http://api.leyou.com/api/item/spec/param/24
     *
     * @param id 规格参数id
     * @return 空
     */
    @DeleteMapping(path = "/param/{id}", name = "删除规格参数")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable(name = "id") Long id) {
        specParamService.deleteSpecParam(id);
        return ResponseEntity.noContent().build();
    }

}
