package com.boyue.item.controller;

import com.boyue.item.dto.SpecParamDTO;
import com.boyue.item.service.BySpecParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Api("商品服务中心SpecParamController")
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
    @ApiOperation(value = "获取规格参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid",value = "规格组id",required = false,dataType = "Integer"),
            @ApiImplicitParam(name = "cid",value = "分类id",required = false,dataType = "Integer"),
            @ApiImplicitParam(name = "searching",value = "是否搜索",required = false,dataType = "Boolean")
    })
    @GetMapping(path = "/spec/params",name = "获取规格参数")
    public ResponseEntity<List<SpecParamDTO>> findSpecParam(@RequestParam(name = "gid", required = false) Long gid,
                                                            @RequestParam(name = "cid", required = false) Long cid,
                                                            @RequestParam(name = "searching", required = false) Boolean searching) {
        log.info("[item-service服务]findSpecParam接口接收到请求,获取规格参数");
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
    @ApiOperation(value = "新增规格参数")
    @PostMapping(path = "/param", name = "新增规格参数")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParamDTO specParamDTO) {
        log.info("[item-service服务]saveSpecParam接口接收到请求,新增规格参数");
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
    @ApiOperation(value = "修改规格参数")
    @PutMapping(path = "/param", name = "修改规格参数")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParamDTO specParamDTO) {
        log.info("[item-service服务]updateSpecParam接口接收到请求,修改规格参数");
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
    @ApiOperation(value = "删除规格参数")
    @ApiImplicitParam(name = "id", value = "规格参数id", required = true, dataType = "Long")
    @DeleteMapping(path = "/param/{id}", name = "删除规格参数")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable(name = "id") Long id) {
        log.info("[item-service服务]deleteSpecParam接口接收到请求, 删除规格参数");
        specParamService.deleteSpecParam(id);
        return ResponseEntity.noContent().build();
    }

}
