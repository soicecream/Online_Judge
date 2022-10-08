package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Constants;
import com.example.demo.entity.Dict;
import com.example.demo.service.IDictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.common.Result;

import com.example.demo.service.IMenuService;
import com.example.demo.entity.Menu;

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
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private IDictService dictService;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        return Result.success(menuService.saveOrUpdate(menu));
    }

    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(menuService.removeById(id));
    }

    // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(menuService.removeByIds(list));
    }

    // 获取所有数据
    @GetMapping
    public Result findAll() {
//         查询所有数据
        List<Menu> list = menuService.list();

        // 找到pid为null的一级菜单
        List<Menu> parentNode = list.stream().filter(menu -> menu.getPid() == 0).collect(Collectors.toList());

        for (Menu menu : parentNode) {
            // 筛选所有的二级菜单  所有数据中pid=父级id的数据就是下级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));

            // 筛选所有的三级菜单
            for(Menu menu1 : menu.getChildren()) {
                menu1.setChildren(list.stream().filter(m -> menu1.getId().equals(m.getPid())).collect(Collectors.toList()));
            }
        }

        return Result.success(parentNode);

//        return Result.success(menuService.list());
    }

    // 根据id查询数据
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
    }

    // 获取所有数据
    @GetMapping("/all")
    public Result findAlls(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();

        if(!"".equals(name)) queryWrapper.like("name", name);

        // 查询所有数据
        List<Menu> list = menuService.list(queryWrapper);

        // 找到pid为null的一级菜单
        List<Menu> parentNode = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());

        // 找出一级菜单的子菜单
        for (Menu menu : parentNode) {
            // 筛选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));

            // 找出二级菜单的子菜单
            for(Menu menus : menu.getChildren()) {
                menus.setChildren(list.stream().filter(mm -> menus.getId().equals(mm.getPid())).collect(Collectors.toList()));
            }
        }

        return Result.success(parentNode);
    }


    // 根据id查询数据
    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictService.list(queryWrapper));
    }


}

