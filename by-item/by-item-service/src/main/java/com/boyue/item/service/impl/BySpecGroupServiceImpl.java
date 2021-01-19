package com.boyue.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.item.dto.SpecGroupDTO;
import com.boyue.item.entity.BySpecGroup;
import com.boyue.item.mapper.BySpecGroupMapper;
import com.boyue.item.service.BySpecGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 规格参数的分组表，每个商品分类下有多个规格参数组 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
@Slf4j
public class BySpecGroupServiceImpl extends ServiceImpl<BySpecGroupMapper, BySpecGroup> implements BySpecGroupService {

    /**
     * 获取规格组
     * @param id 分类id
     * @return 分组的DTO对象的list集合
     */
    @Override
    public List<SpecGroupDTO> findSpec(Long id) {
        //校验参数是否有效
        if (id == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //构建查询条件
        QueryWrapper<BySpecGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BySpecGroup::getCid,id);
        List<BySpecGroup> specGroupList = this.list(queryWrapper);
        //判断查询结果是否为null
        if (CollectionUtils.isEmpty(specGroupList)){
            throw new ByException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        //转换类型返回
        List<SpecGroupDTO> specGroupDTOS = BeanHelper.copyWithCollection(specGroupList, SpecGroupDTO.class);
        if (CollectionUtils.isEmpty(specGroupDTOS)){
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
        return specGroupDTOS;
    }

    /**
     * 新增规格组
     *
     * @param specGroupDTO 规格参数组对象，接收参数
     */
    @Override
    public void saveSpecGroup(SpecGroupDTO specGroupDTO) {
        //校验参数是否有效
        if (specGroupDTO == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        //类型转换
        BySpecGroup specGroup = BeanHelper.copyProperties(specGroupDTO, BySpecGroup.class);

        //存储到数据库
        boolean saveFlag = this.save(specGroup);
        if (!saveFlag){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 删除规格组
     *
     * @param id 规格组id
     */
    @Override
    public void removeSpecGroupById(Long id) {
        if (id == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        boolean flag = this.removeById(id);
        if (!flag){
            throw new ByException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * 修改规格组
     *
     * @param specGroupDTO 修改对象
     */
    @Override
    public void updateSpecGroup(SpecGroupDTO specGroupDTO) {
        if (specGroupDTO == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //转换数据类型
        BySpecGroup specGroup = BeanHelper.copyProperties(specGroupDTO, BySpecGroup.class);
        boolean updateFlag = this.updateById(specGroup);
        if (!updateFlag){
            throw new ByException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }
}
