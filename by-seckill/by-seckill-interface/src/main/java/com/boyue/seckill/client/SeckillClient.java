package com.boyue.seckill.client;

import com.boyue.common.vo.PageResult;
import com.boyue.seckill.dto.SeckillPolicyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 19:06
 * @Author: Jacky
 * @Description: 秒杀服务的feignClient接口
 */
@FeignClient("seckill-service")
public interface SeckillClient {
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
    @GetMapping(path = "/findSecKillPage", name = "分页查询秒杀商品信息")
    PageResult<SeckillPolicyDTO> findSecKillByPage(@RequestParam(name = "key", required = false) String key,
                                                    @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(name = "rows", required = false, defaultValue = "5") Integer rows,
                                                    @RequestParam(name = "state", required = false) Integer state);

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
    @GetMapping(path = "/{id}", name = "根据主键id 查询秒杀商品")
    SeckillPolicyDTO findSecKillPolicyById(@PathVariable(name = "id") Long id);

    /**
     * 根据 秒杀日期 ，查询对应的秒杀列表
     *
     * @param date 秒杀日期
     * @return SeckillPolicyDTO的list集合
     */
    @GetMapping(path = "/list/{date}", name = "根据秒杀日期，查询对应的秒杀列表")
    List<SeckillPolicyDTO> findSecKillPolicyList(@PathVariable(name = "date") String date);

    /**
     * 减库存
     * put /minusStock
     *
     * @param seckillId 秒杀商品id
     * @param num       商品数量
     */
    @PutMapping(path = "/minusStock", name = "减库存")
    Void minusStock(@RequestParam(name = "seckillId") Long seckillId,
                    @RequestParam(name = "num") Integer num);

    /**
     * 恢复秒杀商品库存
     *
     * @param seckillIdAndNumMap 需要回复的参数
     * @return 空
     */
    @PutMapping(path = "/plusStock", name = "恢复秒杀商品库存")
    Void plusStock(@RequestBody Map<Long, Integer> seckillIdAndNumMap);
}
