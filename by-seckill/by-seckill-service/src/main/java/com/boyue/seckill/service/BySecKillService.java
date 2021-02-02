package com.boyue.seckill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.vo.PageResult;
import com.boyue.item.dto.BrandDTO;
import com.boyue.item.dto.CategoryDTO;
import com.boyue.seckill.dto.SeckillPolicyDTO;
import com.boyue.seckill.entity.BySeckillPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:16
 * @Author: Jacky
 * @Description: 秒杀管理平台业务service接口
 */
public interface BySecKillService {

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
    PageResult<SeckillPolicyDTO> findSecKillByPage(Integer page, Integer rows, String key, Integer state);

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
    SeckillPolicyDTO findSecKillById(Long id);

    /**
     * 添加秒杀商品内容
     * POST /
     * - 204：操作成功
     * - 400：参数有误
     * - 500：服务器内部异常
     *
     * @param seckillPolicy 秒杀商品信息
     */
    void addSecKill(BySeckillPolicy seckillPolicy);

    /**
     * 修改秒杀商品信息
     * UPDATE /
     * <p>
     * - 204：操作成功
     * - 400：参数有误
     * - 500：服务器内部异常
     *
     * @param seckillPolicy 秒杀商品信息
     */
    void updateSecKill(BySeckillPolicy seckillPolicy);

    /**
     * 删除秒杀商品信息
     * DELETE /{id}
     *
     * @param id 秒杀的id
     */
    void deleteSecKill(Long id);

    /**
     * 根据 秒杀日期 ，查询对应的秒杀列表
     *
     * @param date 秒杀日期
     * @return SeckillPolicyDTO的list集合
     */
    List<SeckillPolicyDTO> findSecKillPolicyList(String date);

    /**
     * 减库存
     * put /minusStock
     *
     * @param seckillId 秒杀商品id
     * @param num       商品数量
     */
    void minusStock(Long seckillId, Integer num);

    /**
     * 恢复秒杀商品库存
     *
     * @param seckillIdAndNumMap 需要回复的参数
     * @return 空
     */
    void plusStock(Map<Long, Integer> seckillIdAndNumMap);
}
