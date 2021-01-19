package com.boyue.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.item.dto.SpecParamDTO;
import com.boyue.item.entity.BySpecParam;

import java.util.List;

/**
 * <p>
 * 规格参数组下的参数名 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface BySpecParamService extends IService<BySpecParam> {

    /**
     * 获取规格参数
     *
     * @param gid       规格组id
     * @param cid       分类id
     * @param searching 是否搜索
     * @return 规格组的list集合
     */
    List<SpecParamDTO> findSpecParam(Long gid, Long cid, Boolean searching);
}
