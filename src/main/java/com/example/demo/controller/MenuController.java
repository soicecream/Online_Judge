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


    // 获取所有数据的id信息
    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.success(menuService.list().stream().map(Menu::getId));
    }


    // 获取树形接口的菜单数据
    @GetMapping("/findTreeMenus")
    public Result findMenus() {
        return Result.success(menuService.findMenus());
    }


}

