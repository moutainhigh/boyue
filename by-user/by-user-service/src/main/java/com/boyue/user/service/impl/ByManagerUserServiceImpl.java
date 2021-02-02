package com.boyue.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyue.common.enums.ExceptionEnum;
import com.boyue.common.exception.ByException;
import com.boyue.common.utils.BeanHelper;
import com.boyue.common.vo.PageResult;
import com.boyue.user.dto.ManagerUserDTO;
import com.boyue.user.dto.UserDTO;
import com.boyue.user.entity.ByManagerUser;
import com.boyue.user.entity.ByUser;
import com.boyue.user.mapper.ByManagerUserMapper;
import com.boyue.user.service.ByManagerUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author Jacky
 * @since 2021-01-17
 */
@Service
public class ByManagerUserServiceImpl extends ServiceImpl<ByManagerUserMapper, ByManagerUser> implements ByManagerUserService {
    /**
     * spring提供的密码加密策略
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 存放adminUser
     */
    private static final String ADMIN_KEY = "by:admin:user";

    /**
     * 注入redis
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

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
    @Override
    public ManagerUserDTO findAdminUser(String username, String password) {
        //构建查询对象
        QueryWrapper<ByManagerUser> queryWrapper = new QueryWrapper<>();
        //设置查询条件
        queryWrapper.lambda().eq(ByManagerUser::getUsername,username);
        //查询用户
        ByManagerUser adminUser = this.getOne(queryWrapper);

        //校验密码是否正确
        boolean eqFlag = passwordEncoder.matches(password, adminUser.getPassword());
        if (!eqFlag){
            log.error("[by-user服务]findUserByUsernameAndPassword密码不正确");
            throw new ByException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

        return BeanHelper.copyProperties(adminUser, ManagerUserDTO.class);
    }

    /**
     * 查询用户登录信息
     * @return 登录的用户
     */
    @Override
    public ManagerUserDTO findAdminUserInfo() {
        String redisKey = ADMIN_KEY;
        String id = redisTemplate.opsForValue().get(redisKey);
        ByManagerUser managerUser = this.getById(id);
        //类型转换
        return BeanHelper.copyProperties(managerUser,ManagerUserDTO.class);
    }

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
    @Override
    public PageResult<ManagerUserDTO> queryAdminUser(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //构造分页查询条件
        IPage<ByManagerUser> iPage = new Page<>(page, rows);
        //构造查询条件
        QueryWrapper<ByManagerUser> queryWrapper = new QueryWrapper<>();
        //添加查询条件
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.lambda().like(ByManagerUser::getUsername, key).or().like(ByManagerUser::getNickName, key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            if (desc) {
                //倒叙
                queryWrapper.orderByDesc(sortBy);
            } else {
                //正序
                queryWrapper.orderByAsc(sortBy);
            }
        }
        //查询
        IPage<ByManagerUser> userIPage = this.page(iPage, queryWrapper);
        //获取查询结果集
        List<ByManagerUser> userList = userIPage.getRecords();
        if (CollectionUtils.isEmpty(userList)) {
            throw new ByException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        //转换类型
        List<ManagerUserDTO> managerUserDTOList = BeanHelper.copyWithCollection(userList, ManagerUserDTO.class);
        if (CollectionUtils.isEmpty(managerUserDTOList)) {
            throw new ByException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }

        //获取总条数
        long total = userIPage.getTotal();
        //获取总页数
        long pages = userIPage.getPages();

        return new PageResult<ManagerUserDTO>(managerUserDTOList, total, pages);
    }

    /**
     * 新增用户信息
     * POST /userAdmin
     *
     * @param managerUserDTO 用户的dto对象
     */
    @Override
    public void saveManagerUser(ManagerUserDTO managerUserDTO) {
        ByManagerUser managerUser = BeanHelper.copyProperties(managerUserDTO, ByManagerUser.class);
        boolean saveFlag = this.save(managerUser);
        if (!saveFlag){
            throw new ByException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 根据id查询用户信息
     * GET /user/userAdmin/of/edit/
     *
     * @param userId 用户id
     * @return 用户管理员对象
     */
    @Override
    public ManagerUserDTO findAdminUserById(Long userId) {
        if (userId == null){
            throw new ByException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        ByManagerUser managerUser = this.getById(userId);

        return BeanHelper.copyProperties(managerUser,ManagerUserDTO.class);
    }
}
