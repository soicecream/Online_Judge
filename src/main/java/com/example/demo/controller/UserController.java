package com.example.demo.controller;

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
import com.example.demo.exception.ServiceException;
import com.example.demo.service.IUserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

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
@CrossOrigin
public class UserController {

    @Resource
    private IUserService userService;

    // 登录用户
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        return Result.success(userService.login(userDto));
    }

    // 注册用户
    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        return Result.success(userService.register(userDto));
    }


    // 添加一个用户
    @PostMapping("/addOneUser")
    public Result addOneUser(@RequestBody UserDto userDto) {
        return Result.success(userService.addOneUser(userDto));
    }

    // 添加用户列表
    @PostMapping("/addListUser")
    public Result addListUser(@RequestBody UserDto userDto) {
        String text = userDto.getUserListText();
        if (StrUtil.isBlank(text)) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }

        String s[] = text.split("\n");
        List<UserDto> list = new ArrayList<>();
        TreeSet<String> tr = new TreeSet<>();
        for (String item : s) {
            int n = item.length();
            if (n < 6 || !tr.add(item)) {
                continue;
            }

            UserDto user = new UserDto();
            user.setUsername(item);
            user.setPassword("zime" + item.substring(n - 6));
            user.setRealname(item);
            user.setNickname(item);
            list.add(user);
        }

        return Result.success(userService.addListUser(list));
    }


    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.MyRemoveById(id));
    }

    // 以id列表来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(userService.MyRemoveByIds(list));
    }


    // 更新
    @PostMapping("/update")
    public Result updateUser(@RequestBody User user) {
        return Result.success(userService.updateById(user));
    }

    // 批量更新
    @PostMapping("/update/batch")
    public Result updateBath(@RequestBody Collection<User> list) {
        return Result.success(userService.updateBatchById(list));
    }

    // 修改用户密码
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UserDto userDto) {
        return Result.success(userService.updatePassword(userDto));
    }

    // 重置用户密码
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody UserDto userDto) {
        return Result.success(userService.resetPassword(userDto));
    }


    // 根据id查询数据
    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    // 根据username查询数据
    @GetMapping("/username/{username}")
    public Result findOneByUsername(@PathVariable String username) {
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


    //    https://hutool.cn/docs/#/poi/Excel%E7%94%9F%E6%88%90-ExcelWriter
    // 导出用户
    @GetMapping("/exportUserList")
    public void exportFile(HttpServletResponse response) throws Exception {
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

    // Excel导入用户信息
    @PostMapping("/import/userList")
    public Result importFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return Result.error(Constants.CODE_400, "请选择文件");
        }

        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

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


        List<UserDto> list = reader.readAll(UserDto.class);
        for (UserDto i : list) {
            // 如果用户的密码为空就设一个初始值
            if (StrUtil.isBlank(i.getUsername())) {
                return Result.error("400", "请在excel表中添加字段为用户名或者username的字段");
            }
            // 若密码不存在 则定义初始密码
            if (StrUtil.isBlank(i.getPassword())) {
                String username = i.getUsername();
                i.setPassword("zime" + username.substring(username.length() - 6));
            }
            if (StrUtil.isBlank(i.getNickname())) {
                i.setNickname(i.getUsername());
            }
            if (StrUtil.isBlank(i.getRealname())) {
                i.setRealname(i.getUsername());
            }
        }

        return Result.success(userService.addListUser(list));
    }

}

