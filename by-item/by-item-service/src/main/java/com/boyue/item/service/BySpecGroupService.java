package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.item.dto.SpecGroupDTO;
import com.boyue.item.entity.BySpecGroup;

import java.util.List;

/**
 * <p>
 * 规格参数的分组表，每个商品分类下有多个规格参数组 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface BySpecGroupService extends IService<BySpecGroup> {

    /**
     * 获取规格组
     *
     * @param id 分类id
     * @return 分组的DTO对象的list集合
     */
    List<SpecGroupDTO> findSpec(Long id);

    /**
     * 新增规格组
     *
     * @param specGroupDTO 规格参数组对象，接收参数
     */
    void saveSpecGroup(SpecGroupDTO specGroupDTO);

    /**
     * 删除规格组
     *
     * @param id 规格组id
     */
    void removeSpecGroupById(Long id);

    /**
     * 修改规格组
     *
     * @param specGroupDTO 修改对象
     */
    void updateSpecGroup(SpecGroupDTO specGroupDTO);

    /**
     * 通过分类id查询商品的规格组和组内参数
     *
     * @param id 分类id
     * @return specGroupDTO的list集合
     */
    List<SpecGroupDTO> findSpecParamAndSpecGroup(Long id);
}
