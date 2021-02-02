package com.boyue.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyue.common.vo.PageResult;
import com.boyue.user.dto.ManagerUserDTO;
import com.boyue.user.dto.UserDTO;
import com.boyue.user.entity.ByManagerUser;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
public interface ByManagerUserService extends IService<ByManagerUser> {

    /**
     * 根据用户名和密码查询用户：
     * 管理系统的登录功能
     * 查询功能，根据参数中的用户名和密码查询指定用户并且返回用户
     * 路径接口：
     * GET /query
     *
     * @param username 用户名
     * @param password 用户密码
     * @return userDTO对象
     */
    ManagerUserDTO findAdminUser(String username, String password);

    /**
     * 查询用户登录信息
     * @return 登录的用户
     */
    ManagerUserDTO findAdminUserInfo();

    /**
     * 功能说明:  获取用户列表
     * GET adminUser/page?key=&page=1&rows=5&sortBy=id&desc=false
     *
     * @param key    搜索的关键词
     * @param page   当前页码
     * @param rows   每页显示条数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @return 查询到的品牌的对象集合
     */
    PageResult<ManagerUserDTO> queryAdminUser(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 新增用户信息
     * POST /userAdmin
     *
     * @param managerUserDTO 用户的dto对象
     */
    void saveManagerUser(ManagerUserDTO managerUserDTO);

    /**
     * 根据id查询用户信息
     * GET /user/userAdmin/of/edit/
     *
     * @param userId 用户id
     * @return 用户管理员对象
     */
    ManagerUserDTO findAdminUserById(Long userId);
}
