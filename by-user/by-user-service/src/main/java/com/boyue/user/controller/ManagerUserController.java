package com.boyue.user.controller;

import com.boyue.common.vo.PageResult;
import com.boyue.user.dto.ManagerUserDTO;
import com.boyue.user.dto.UserDTO;
import com.boyue.user.service.ByManagerUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/31 14:21
 * @Author: Jacky
 * @Description: 管理系统的用户中心
 */
@RestController
@Api("管理系统的用户中心ManagerUserController")
@Slf4j
public class ManagerUserController {

    /**
     * 注入管理系统的service
     */
    @Autowired
    private ByManagerUserService managerUserService;

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
    @ApiOperation(value = "根据参数中的用户名和密码查询指定用户")
    @GetMapping(path = "/queryAdminUser",name = "根据参数中的用户名和密码查询指定用户")
    public ResponseEntity<ManagerUserDTO> findAdminUser(@RequestParam(name = "username") String username,
                                                        @RequestParam(name = "password") String password) {
        log.info("[by-user服务]findAdminUser接口接收到请求,正在完成查询功能");
        return ResponseEntity.ok(managerUserService.findAdminUser(username, password));
    }

    /**
     * 查询用户信息
     * GET /findUser
     *
     */
    @ApiOperation(value = "查询用户信息")
    @GetMapping(path = "/userAdmin/findAdminUserInfo",name = "查询用户信息")
    public ResponseEntity<ManagerUserDTO> findAdminUserInfo() {
        log.info("[by-user服务]findUserByUsernameAndPassword接口接收到请求,正在完成查询功能");
        return ResponseEntity.ok(managerUserService.findAdminUserInfo());
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
    @ApiOperation(value = "获取品牌列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key",value = "搜索的关键词",required = false,dataType = "String"),
            @ApiImplicitParam(name = "page",value = "当前页码",required = false,dataType = "Integer",defaultValue = "1"),
            @ApiImplicitParam(name = "rows",value = "每页显示条数",required = false,dataType = "Integer",defaultValue = "10"),
            @ApiImplicitParam(name = "sortBy",value = "排序字段",required = false,dataType = "String"),
            @ApiImplicitParam(name = "desc",value = "是否降序",required = false,dataType = "Boolean",defaultValue = "false")
    })
    @GetMapping(path = "/userAdmin/page", name = "获取品牌列表")
    public ResponseEntity<PageResult<ManagerUserDTO>> queryAdminUser(@RequestParam(name = "key", required = false) String key,
                                                                       @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                       @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
                                                                       @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                       @RequestParam(name = "desc", required = false, defaultValue = "false") Boolean desc) {
        log.info("调用findCategoryList接口");
        PageResult<ManagerUserDTO> pageResult = managerUserService.queryAdminUser(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 新增用户信息
     * POST /userAdmin
     *
     * @param managerUserDTO 用户的dto对象
     * @return 空
     */
    @ApiOperation(value = "新增用户信息")
    @PostMapping(path = "/userAdmin", name = "新增用户信息")
    public ResponseEntity<Void> saveManagerUser(@RequestBody ManagerUserDTO managerUserDTO) {
        log.info("调用 saveManagerUser 接口");
        managerUserService.saveManagerUser(managerUserDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据id查询用户信息
     * GET /user/userAdmin/of/edit/
     *
     * @param userId 用户id
     * @return 用户管理员对象
     */
    @ApiOperation(value = "根据id查询用户信息")
    @GetMapping(path = "/userAdmin/of/edit", name = "根据id查询用户信息")
    public ResponseEntity<ManagerUserDTO> findAdminUserById(@RequestParam(name = "id") Long userId) {
        log.info("调用 findBrandById 接口");
        ManagerUserDTO managerUserDTO = managerUserService.findAdminUserById(userId);
        return ResponseEntity.ok(managerUserDTO);
    }

    /**
     * 根据id删除用户信息
     * DELETE http://api.boyue.com/api/user/userAdmin/1
     *
     * @param id 用户id
     * @return 空
     */
    @ApiOperation(value = "根据id删除用户信息")
    @DeleteMapping(path = "/userAdmin/delete/{id}", name = "根据id删除用户信息")
    public ResponseEntity<Void> deleteAdminUserById(@PathVariable(value = "id") Long id) {
        log.info("调用 deleteAdminUserById 接口");
        managerUserService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
