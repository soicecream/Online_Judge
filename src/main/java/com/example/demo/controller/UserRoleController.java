package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.demo.common.Result;

import com.example.demo.service.IUserRoleService;
import com.example.demo.entity.UserRole;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2022-10-29
 */
@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    @Resource
    private IUserRoleService userRoleService;

    // 新增
    @PostMapping("/addRole")
    public Result addRole(@RequestBody UserRole userRole) {
        return Result.success(userRoleService.addRole(userRole));
    }


    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userRoleService.removeById(id));
    }

    // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(userRoleService.removeByIds(list));
    }


    // 修改
    @PostMapping("/update")
    public Result updateRole(@RequestBody UserRole userRole) {
        return Result.success(userRoleService.updateRole(userRole));
    }


    // 分页查询     limit第一个参数 = (pageNum - 1) * pageSize
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String userUsername, @RequestParam(defaultValue = "") String userRealname, @RequestParam(defaultValue = "") Integer roleId, @RequestParam(defaultValue = "false") Boolean desc) {
        return Result.success(userRoleService.findPage(new Page<>(pageNum, pageSize), userUsername, userRealname, roleId, desc));
    }

}

