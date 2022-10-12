package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.service.IUserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2022-09-25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;



    // 登录用户
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.login(userDto));
    }

    // 注册用户
    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDto));
    }

    // 修改用户密码
    @PostMapping("/upadtePassword")
    public Result updatePassword(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.updatePassword(userDto));
    }

    // 重置用户密码
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        if (StrUtil.isBlank(username)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.resetPassword(userDto));
    }


    // 获取所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    // 根据id查询数据
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    // 根据username查询数据
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    // 分页查询   limit第一个参数 = (pageNum - 1) * pageSize
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String realname, @RequestParam(defaultValue = "") Integer sex, @RequestParam(defaultValue = "") String address, @RequestParam(defaultValue = "") Integer enable, @RequestParam(defaultValue = "") Integer isRank, @RequestParam(defaultValue = "false") Boolean desc) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (desc) queryWrapper.orderByDesc("id"); // 是否根据id排序

        if (!"".equals(username)) queryWrapper.like("username", username);
        if (!"".equals(realname)) queryWrapper.like("realname", realname);
        if (sex != null) queryWrapper.eq("sex", sex);
        if (!"".equals(address)) queryWrapper.like("address", address);
        if (enable != null) queryWrapper.eq("enable", enable);
        if (isRank != null) queryWrapper.eq("is_rank", isRank);

        // 获取当前用户信息
        User currentUser = TokenUtils.getCurrenUser();
        if (currentUser != null) {
            System.out.println(" 获取当前用户信息的昵称 ============== " + currentUser.getNickname());
        }

        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {

        return Result.success(userService.saveOrUpdate(user));
    }

    // 批量新增或者更新
    @PostMapping("/batch")
    public Result saveBath(@RequestBody Collection<User> list) {
        return Result.success(userService.saveOrUpdateBatch(list));
    }


    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(userService.removeByIds(list));
    }


    //    https://hutool.cn/docs/#/poi/Excel%E7%94%9F%E6%88%90-ExcelWriter
    // 导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");

        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.setOnlyAlias(true);
        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("realname", "姓名");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("phone", "手机号");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("school", "学校");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("introduction", "介绍");
        writer.addHeaderAlias("headPortrait", "头像");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("language", "语言");
        writer.addHeaderAlias("enable", "状态");
        writer.addHeaderAlias("isRank", "是否参加排名");
        writer.addHeaderAlias("isDelete", "假删除");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        // 设置content—type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        // 设置标题
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
//        Content-disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();

//        将Writer刷新到OutPut
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result importFile(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

//        通过javaben 的方式读取excel内的对象 但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);
//        userService.saveBatch(list);


        reader.addHeaderAlias("用户名", "username");
        reader.addHeaderAlias("密码", "password");
        reader.addHeaderAlias("姓名", "realname");
        reader.addHeaderAlias("昵称", "nickname");
        reader.addHeaderAlias("性别", "sex");
        reader.addHeaderAlias("手机号", "phone");
        reader.addHeaderAlias("邮箱", "email");
        reader.addHeaderAlias("学校", "school");
        reader.addHeaderAlias("地址", "address");
        reader.addHeaderAlias("介绍", "introduction");
        reader.addHeaderAlias("头像", "headPortrait");
        reader.addHeaderAlias("创建时间", "createTime");
        reader.addHeaderAlias("语言", "language");
        reader.addHeaderAlias("状态", "enable");
        reader.addHeaderAlias("是否参加排名", "isRank");
        reader.addHeaderAlias("假删除", "isDelete");

        List<User> list = reader.readAll(User.class);
        for (User i : list) {
            // 如果用户的密码为空就设一个初始值
//            System.out.println("=================>");
//            System.out.println(i.getUsername());
            if (StrUtil.isBlank(i.getUsername())) {
//                System.out.println("=============> no");
                return Result.error("400", "请在excel表中添加用户名或者username字段");
            }
            if (StrUtil.isBlank(i.getPassword())) {
                i.setPassword("123456");
            }
            if (StrUtil.isBlank(i.getNickname())) {
                i.setNickname(i.getUsername());
            }
        }
        userService.saveBatch(list);

        return Result.success(true);
    }

}

