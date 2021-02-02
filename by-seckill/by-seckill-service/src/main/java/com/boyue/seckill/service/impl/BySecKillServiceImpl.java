package com.boyue.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.vo.PageResult;
import com.boyue.item.client.BrandClient;
import com.boyue.item.client.CategoryClient;
import com.boyue.item.dto.BrandDTO;
import com.boyue.item.dto.CategoryDTO;
import com.boyue.seckill.dto.SeckillPolicyDTO;
import com.boyue.seckill.entity.BySeckillPolicy;
import com.boyue.seckill.service.BySecKillRedisService;
import com.boyue.seckill.service.BySecKillService;
import com.boyue.seckill.service.BySeckillPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/30 16:16
 * @Author: Jacky
 * @Description: 秒杀管理平台业务service接口实现类
 */
@Slf4j
@Service
public class BySecKillServiceImpl implements BySecKillService {

    /**
     * 注入秒杀政策表 服务类 BySeckillPolicyService
     */
    @Autowired
    private BySeckillPolicyService seckillPolicyService;

    /**
     * 注入item服务的brand品牌的feign接口
     */
    @Autowired
    private BrandClient brandClient;

    /**
     * 注入item服务的category分类的feign接口
     */
    @Autowired
    private CategoryClient categoryClient;

    /**
     * 注入秒杀的 redis 操作的service
     */
    @Autowired
    private BySecKillRedisService secKillRedisService;

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
    @Override
    public PageResult<SeckillPolicyDTO> findSecKillByPage(Integer page, Integer rows, String key, Integer state) {

        IPage<BySeckillPolicy> iPage = new Page<>(page, rows);
        QueryWrapper<BySeckillPolicy> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.lambda().like(BySeckillPolicy::getTitle, key);
        }
        if (state != null) {
            Date now = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(now);
            switch (state) {
                case 1:
                    queryWrapper.lambda().gt(BySeckillPolicy::getBeginTime, time);
                    break;
                case 2:
                    queryWrapper.lambda().lt(BySeckillPolicy::getBeginTime, time)
                            .gt(BySeckillPolicy::getEndTime, time);
                    break;
                case 3:
                    queryWrapper.lambda().lt(BySeckillPolicy::getEndTime, time);
                    break;
                default:
                    throw new ByException(ExceptionEnum.SECKILL_NOT_FOUND);
            }
        }
        queryWrapper.orderByAsc("begin_time");
        IPage<BySeckillPolicy> policyIPage = seckillPolicyService.page(iPage, queryWrapper);
        if (policyIPage == null || CollectionUtils.isEmpty(policyIPage.getRecords())) {
            throw new ByException(ExceptionEnum.SECKILL_NOT_FOUND);
        }

        List<BySeckillPolicy> seckillPolicyList = policyIPage.getRecords();
        List<SeckillPolicyDTO> seckillPolicyDTOS = BeanHelper.copyWithCollection(seckillPolicyList, SeckillPolicyDTO.class);
        this.handleCategoryAndBrandName(seckillPolicyDTOS);
        return new PageResult<SeckillPolicyDTO>(seckillPolicyDTOS, policyIPage.getTotal(), policyIPage.getPages());
    }

    /**
     * 处理 品牌名称 和 分类名称
     *
     * @param seckillPolicyDTOS 秒伤商品的list集合
     */
    private void handleCategoryAndBrandName(List<SeckillPolicyDTO> seckillPolicyDTOS) {

        for (SeckillPolicyDTO seckillPolicyDTO : seckillPolicyDTOS) {
            //通过brandId查询brandName
            Long brandId = seckillPolicyDTO.getBrandId();
            BrandDTO brandDTO = brandClient.findBrandListById(brandId);
            seckillPolicyDTO.setBrandName(brandDTO.getName());
            //查询categoryName  结果如 ：手机/手机通讯/手机    1级分类名/2级分类名/3级分类名
            List<Long> cids = seckillPolicyDTO.getCategorys();
            List<CategoryDTO> categoryDTOList = categoryClient.findCategoryByIds(cids);
            String categoryNames = categoryDTOList.stream().map(CategoryDTO::getName).collect(Collectors.joining("/"));
            seckillPolicyDTO.setCategoryName(categoryNames);
        }
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
    @Override
    public SeckillPolicyDTO findSecKillById(Long id) {
        BySeckillPolicy seckillPolicy = seckillPolicyService.getById(id);
        if (seckillPolicy == null) {
            throw new ByException(ExceptionEnum.SECKILL_NOT_FOUND);
        }
        return BeanHelper.copyProperties(seckillPolicy, SeckillPolicyDTO.class);
    }



    /**
     * 添加秒杀商品内容
     * POST /
     * - 204：操作成功
     * - 400：参数有误
     * - 500：服务器内部异常
     *
     * @param seckillPolicy 秒杀商品信息
     */
    @Override
    public void addSecKill(BySeckillPolicy seckillPolicy) {
        //获取秒杀日期 yyyy-MM-dd
        String secKillDay = new DateTime(seckillPolicy.getBeginTime()).toString("yyyy-MM-dd");
        //设置秒杀库存
        seckillPolicy.setStockCount(seckillPolicy.getNum());
        //设置秒杀 结束时间，默认为 开始2个小时后结束
        Date endTime = new DateTime(seckillPolicy.getBeginTime()).plusHours(2).toDate();
        seckillPolicy.setEndTime(endTime);
        //设置秒杀 日期
        seckillPolicy.setSecKillDate(secKillDay);
        boolean b = seckillPolicyService.save(seckillPolicy);
        if (!b) {
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        //秒杀id
        Long secKillId = seckillPolicy.getId();
        //查询秒杀信息
        BySeckillPolicy bySeckillPolicy = seckillPolicyService.getById(secKillId);
        SeckillPolicyDTO seckillPolicyDTO = BeanHelper.copyProperties(bySeckillPolicy, SeckillPolicyDTO.class);
        //添加秒杀商品的缓存
        secKillRedisService.putSeckillPolicyToRedis(seckillPolicyDTO);
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
     */
    @Override
    public void updateSecKill(BySeckillPolicy seckillPolicy) {
        //获取秒杀日期 yyyy-MM-dd
        String secKillDay = new DateTime(seckillPolicy.getBeginTime()).toString("yyyy-MM-dd");
        // 设置秒杀库存
        seckillPolicy.setStockCount(seckillPolicy.getNum());
        //设置秒杀 结束时间，默认为 开始2个小时后结束
        Date endTime = new DateTime(seckillPolicy.getBeginTime()).plusHours(2).toDate();
        seckillPolicy.setEndTime(endTime);
        //设置秒杀 日期
        seckillPolicy.setSecKillDate(secKillDay);
        boolean b = seckillPolicyService.updateById(seckillPolicy);
        if (!b) {
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        Long secKillId = seckillPolicy.getId();
        //删除秒杀的redis信息
        secKillRedisService.removeSeckill(secKillId);
        //查询秒杀信息
        BySeckillPolicy bySeckillPolicy = seckillPolicyService.getById(secKillId);
        SeckillPolicyDTO seckillPolicyDTO = BeanHelper.copyProperties(bySeckillPolicy, SeckillPolicyDTO.class);
        //添加秒杀商品的缓存
        secKillRedisService.putSeckillPolicyToRedis(seckillPolicyDTO);
    }

    /**
     * 删除秒杀商品信息
     * DELETE /{id}
     *
     * @param id 秒杀的id
     */
    @Override
    public void deleteSecKill(Long id) {
        //先把要删除的数据查询出来
        BySeckillPolicy seckillPolicy = seckillPolicyService.getById(id);
        boolean b = seckillPolicyService.removeById(id);
        if (!b) {
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }

        secKillRedisService.removeSeckill(id);
    }

    /**
     * 根据 秒杀日期 ，查询对应的秒杀列表
     *
     * @param date 秒杀日期
     * @return SeckillPolicyDTO的list集合
     */
    @Override
    public List<SeckillPolicyDTO> findSecKillPolicyList(String date) {
        QueryWrapper<BySeckillPolicy> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BySeckillPolicy::getSecKillDate, date);
//        查询集合
        List<BySeckillPolicy> list = seckillPolicyService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new ByException(ExceptionEnum.SECKILL_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, SeckillPolicyDTO.class);
    }

    /**
     * 减库存
     * put /minusStock
     *
     * @param seckillId 秒杀商品id
     * @param num       商品数量
     */
    @Override
    public void minusStock(Long seckillId, Integer num) {
        int code = seckillPolicyService.minusStock(seckillId, num);
        if (code != 1) {
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
     * 恢复秒杀商品库存
     *
     * @param seckillIdAndNumMap 需要回复的参数
     */
    @Override
    public void plusStock(Map<Long, Integer> seckillIdAndNumMap) {
        //恢复mysql的库存
        this.seckillPolicyService.plusStock(seckillIdAndNumMap);
        //恢复redis的库存
        secKillRedisService.plusStock(seckillIdAndNumMap);
    }
}
