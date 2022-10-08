package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.example.demo.common.Result;

import com.example.demo.service.IRoleMenuService;
import com.example.demo.entity.RoleMenu;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色菜单关系表 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2022-10-08
 */
@RestController
@RequestMapping("/role-menu")
public class RoleMenuController {

        @Resource
        private IRoleMenuService roleMenuService;


        // 新增或者更新
        @PostMapping
        public Result save(@RequestBody RoleMenu roleMenu) {
                return Result.success(roleMenuService.saveOrUpdate(roleMenu));
        }

        // 根据id删除
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
                return Result.success(roleMenuService.removeById(id));
        }

        // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> list) {
                return Result.success(roleMenuService.removeByIds(list));
        }

        // 获取所有数据
        @GetMapping
        public Result findAll() {
                return Result.success(roleMenuService.list());
        }

        // 根据id查询数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(roleMenuService.getById(id));
        }

        // 分页查询     limit第一个参数 = (pageNum - 1) * pageSize
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "false") Boolean desc) {
                QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
                if(desc) queryWrapper.orderByDesc("id"); // 是否根据id排序

                return Result.success(roleMenuService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

}

