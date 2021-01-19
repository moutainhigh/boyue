package com.boyue.item.controller;

import com.boyue.item.dto.SpecGroupDTO;
import com.boyue.item.service.BySpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/18 17:10
 * @Author: Jacky
 * @Description: 规格组specGroup的controller层
 */
@RestController
public class SpecGroupController {

    @Autowired
    private BySpecGroupService specGroupService;

    /**
     * 获取规格组
     * 请求路径  GET /spec/groups/of/category?id=76
     *
     * @param id 分类id
     * @return 规格组的list集合
     */
    @GetMapping(path = "/spec/groups/of/category", name = "获取规格组")
    public ResponseEntity<List<SpecGroupDTO>> findSpecGroupById(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(specGroupService.findSpec(id));
    }

    /**
     * 新增规格组
     * POST /spec/group
     *
     * @param specGroupDTO 规格参数组对象，接收参数
     *                     cid   分类id
     *                     name  规格组名称
     * @return 空
     */
    @PostMapping(path = "/spec/group", name = "新增规格组")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroupDTO specGroupDTO) {
        specGroupService.saveSpecGroup(specGroupDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除规格组
     * DELETE http://api.boyue.com/api/item/spec/group/18
     *
     * @param id 规格组id
     * @return 空
     */
    @DeleteMapping(path = "/spec/group/{id}", name = "删除规格组")
    public ResponseEntity<Void> removeSpecGroupById(@PathVariable(value = "id") Long id) {
        specGroupService.removeSpecGroupById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改规格组
     * 请求路径  PUT /spec/group
     *
     * @param specGroupDTO 修改对象
     * @return 空
     */
    @PutMapping(path = "/spec/group", name = "修改规格组")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroupDTO specGroupDTO) {
        specGroupService.updateSpecGroup(specGroupDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 通过分类id查询商品的规格组和组内参数
     *
     * @param id 分类id
     * @return specGroupDTO的list集合
     */
    @GetMapping(path = "/{id}", name = "通过分类id查询商品的规格组和组内参数")
    public ResponseEntity<List<SpecGroupDTO>> findSpecParamAndSpecGroup(@PathVariable(name = "id") Long id) {
        List<SpecGroupDTO> list = specGroupService.findSpecParamAndSpecGroup(id);
        return ResponseEntity.ok(list);
    }
}
