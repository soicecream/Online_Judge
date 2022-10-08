package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.example.demo.common.Result;

import com.example.demo.service.IFileService;
import com.example.demo.entity.File;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 作者
 * @since 2022-10-07
 */
@RestController
@RequestMapping("/file")
public class FileController {

        @Resource
        private IFileService fileService;


        // 新增或者更新
        @PostMapping
        public Result save(@RequestBody File file) {
                return Result.success(fileService.saveOrUpdate(file));
        }

        // 根据id删除
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
                return Result.success(fileService.removeById(id));
        }

        // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> list) {
                return Result.success(fileService.removeByIds(list));
        }

        // 获取所有数据
        @GetMapping
        public Result findAll() {
                return Result.success(fileService.list());
        }

        // 根据id查询数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(fileService.getById(id));
        }

        // 分页查询     limit第一个参数 = (pageNum - 1) * pageSize
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "false") Boolean desc) {
                QueryWrapper<File> queryWrapper = new QueryWrapper<>();
                if(desc) queryWrapper.orderByDesc("id"); // 是否根据id排序

                return Result.success(fileService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

}

