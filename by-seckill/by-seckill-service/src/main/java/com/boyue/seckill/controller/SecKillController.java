package com.boyue.seckill.controller;

import com.boyue.common.vo.PageResult;
import com.boyue.seckill.dto.SeckillPolicyDTO;
import com.boyue.seckill.entity.BySeckillPolicy;
import com.boyue.seckill.service.BySecKillService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:14
 * @Author: Jacky
 * @Description: 秒杀 后台管理操作
 */
@RestController
@Slf4j
public class SecKillController {
    /**
     * 注入秒杀的service
     */
    @Autowired
    private BySecKillService secKillService;

    /**
     * 分页查询秒杀商品信息
     * GET /findSecKillPage
     * <p>
     * - 200：请求处理成功
     * - 400：参数有误
     * - 500：服务器内部异常
     *
     * @param key   搜索关键词
     * @param page  当前页码
     * @param rows  每页显示条数
     * @param state 状态 1-未开始 2-进行中  3-已结束
     * @return SeckillPolicyDTO
     */
    @ApiOperation(value = "分页查询秒杀商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "搜索关键词", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页显示条数", dataType = "Integer"),
            @ApiImplicitParam(name = "state", value = "状态", dataType = "Integer")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求处理成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "服务器内部异常")
    })
    @GetMapping(path = "/findSecKillPage", name = "分页查询秒杀商品信息")
    public ResponseEntity<PageResult<SeckillPolicyDTO>> findSecKillByPage(@RequestParam(name = "key", required = false) String key,
                                                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                          @RequestParam(name = "rows", required = false, defaultValue = "5") Integer rows,
                                                                          @RequestParam(name = "state", required = false) Integer state) {
        log.info("[by-seckill服务]findSecKillByPage接口接收到请求,分页查询秒杀商品信息");
        return ResponseEntity.ok(secKillService.findSecKillByPage(page, rows, key, state));
    }

    /**
     * 根据主键id 查询一条秒杀商品信息
     * GET  /{id}
     * - 200：请求处理成功
     * - 400：参数有误
     * - 500：服务器内部异常
     *
     * @param id 秒杀商品主键id
     * @return SeckillPolicyDTO
     */
    @ApiOperation(value = "根据主键id 查询秒杀商品")
    @ApiImplicitParam(name = "id", value = "秒杀商品主键id", dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求处理成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "服务器内部异常")
    })
    @GetMapping(path = "/{id}", name = "根据主键id 查询秒杀商品")
    public ResponseEntity<SeckillPolicyDTO> findSecKillPolicyById(@PathVariable(name = "id") Long id) {
        log.info("[by-seckill服务] findSecKillPolicyById 接口接收到请求,根据主键id 查询秒杀商品");
        return ResponseEntity.ok(secKillService.findSecKillById(id));
    }

    /**
     * 添加秒杀商品内容
     * POST /
     * - 204：操作成功
     * - 400：参数有误
     * - 500：服务器内部异常
     *
     * @param seckillPolicy 秒杀商品信息
     * @return 空
     */
    @ApiOperation(value = "添加秒杀商品内容")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求处理成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "服务器内部异常")
    })
    @PostMapping(name = "添加秒杀商品内容")
    public ResponseEntity<Void> addSecKill(@RequestBody BySeckillPolicy seckillPolicy) {
        log.info("[by-seckill服务] addSecKill 接口接收到请求,添加秒杀商品内容");
        secKillService.addSecKill(seckillPolicy);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 修改秒杀商品信息
     * UPDATE /
     * <p>
     * - 204：操作成功
     * - 400：参数有误
     * - 500：服务器内部异常
     *
     * @param seckillPolicy 秒杀商品信息
     * @return 空
     */
    @ApiOperation(value = "修改秒杀商品信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求处理成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "服务器内部异常")
    })
    @PutMapping(name = "修改秒杀商品信息")
    public ResponseEntity<Void> updateSecKill(@RequestBody BySeckillPolicy seckillPolicy) {
        log.info("[by-seckill服务] updateSecKill 接口接收到请求,修改秒杀商品信息");
        secKillService.updateSecKill(seckillPolicy);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除秒杀商品信息
     * DELETE /{id}
     *
     * @param id 秒杀的id
     * @return 空
     */
    @ApiOperation(value = "删除秒杀商品信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求处理成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "服务器内部异常")
    })
    @DeleteMapping(path = "/{id}", name = "删除秒杀商品信息")
    public ResponseEntity<Void> deleteSecKill(@PathVariable(name = "id") Long id) {
        log.info("[by-seckill服务] deleteSecKill 接口接收到请求,删除秒杀商品信息");
        secKillService.deleteSecKill(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据 秒杀日期 ，查询对应的秒杀列表
     *
     * @param date 秒杀日期
     * @return SeckillPolicyDTO的list集合
     */
    @ApiOperation(value = "根据秒杀日期，查询对应的秒杀列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求处理成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "服务器内部异常")
    })
    @GetMapping(path = "/list/{date}", name = "根据秒杀日期，查询对应的秒杀列表")
    public ResponseEntity<List<SeckillPolicyDTO>> findSecKillPolicyList(@PathVariable(name = "date") String date) {
        log.info("[by-seckill服务] findSecKillPolicyList 接口接收到请求,根据秒杀日期查询对应的秒杀列表");
        List<SeckillPolicyDTO> seckillPolicyDTOList = secKillService.findSecKillPolicyList(date);
        return ResponseEntity.ok(seckillPolicyDTOList);
    }

    /**
     * 减库存
     * put /minusStock
     *
     * @param seckillId 秒杀商品id
     * @param num       商品数量
     */
    @ApiOperation(value = "减库存")
    @PutMapping(path = "/minusStock", name = "减库存")
    public ResponseEntity<Void> minusStock(@RequestParam(name = "seckillId") Long seckillId,
                                           @RequestParam(name = "num") Integer num) {
        log.info("[by-seckill服务] minusStock 接口接收到请求,减库存");
        secKillService.minusStock(seckillId, num);
        return ResponseEntity.noContent().build();
    }

    /**
     * 恢复秒杀商品库存
     *
     * @param seckillIdAndNumMap 需要回复的参数
     * @return 空
     */
    @ApiOperation(value = "恢复秒杀商品库存")
    @PutMapping(path = "/plusStock", name = "恢复秒杀商品库存")
    public ResponseEntity<Void> plusStock(@RequestBody Map<Long, Integer> seckillIdAndNumMap) {
        secKillService.plusStock(seckillIdAndNumMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

