package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.item.dto.SpecParamDTO;
import com.boyue.item.entity.BySpecParam;
import com.boyue.item.mapper.BySpecParamMapper;
import com.boyue.item.service.BySpecParamService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 规格参数组下的参数名 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class BySpecParamServiceImpl extends ServiceImpl<BySpecParamMapper, BySpecParam> implements BySpecParamService {

    /**
     * 获取规格参数
     *
     * @param gid       规格组id
     * @param cid       分类id
     * @param searching 是否搜索
     * @return 规格组的list集合
     */
    @Override
    public List<SpecParamDTO> findSpecParam(Long gid, Long cid, Boolean searching) {
        //构建查询条件
        QueryWrapper<BySpecParam> queryWrapper = new QueryWrapper<>();
        //判断参数，设置查询条件
        //判断规格组id
        if (gid != null){
            queryWrapper.lambda().eq(BySpecParam::getGroupId,gid);
        }
        //判断分类id
        if (cid != null){
            queryWrapper.lambda().eq(BySpecParam::getCid,cid);
        }
        //判断是否搜索
        if (searching != null){
            queryWrapper.lambda().eq(BySpecParam::getSearching,searching);
        }

        //查询结果集
        List<BySpecParam> specParamList = this.list(queryWrapper);

        if (CollectionUtils.isEmpty(specParamList)){
            throw new ByException(ExceptionEnum.SPEC_NOT_FOUND);
        }

        List<SpecParamDTO> specParamDTOList = BeanHelper.copyWithCollection(specParamList, SpecParamDTO.class);
        if (CollectionUtils.isEmpty(specParamDTOList)){
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        return specParamDTOList;
    }
}
