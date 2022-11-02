package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.RoleMenu;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.common.Result;

import com.example.demo.service.IRoleService;
import com.example.demo.entity.Role;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2022-10-06
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Role role) {
        return Result.success(roleService.saveOrUpdate(role));
    }

    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(roleService.removeById(id));
    }

    // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(roleService.removeByIds(list));
    }

    // 根据id查询数据
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    // 分页查询     limit第一个参数 = (pageNum - 1) * pageSize
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String description, @RequestParam(defaultValue = "") String roleKey, @RequestParam(defaultValue = "false") Boolean desc) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (desc) queryWrapper.orderByDesc("id"); // 是否根据id排序

        if (!"".equals(name)) queryWrapper.like("name", name);
        if (!"".equals(description)) queryWrapper.like("description", description);
        if (!"".equals(roleKey)) queryWrapper.like("role_key", roleKey);


        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 绑定角色和菜单的关系
     *
     * @param roleId  角色id
     * @param menuIds 菜单id数据
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result saveRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        return Result.success(roleService.saveRoleMenu(roleId, menuIds));
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success(roleService.getRoleMenu(roleId).stream().map(RoleMenu::getMenuId).collect(Collectors.toList()));
    }

}

