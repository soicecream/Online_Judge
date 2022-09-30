package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.UserDto;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.service.IUserService;
import com.example.demo.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        UserDto dto = userService.login(userDto);
        return Result.success(dto);
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


    // 新增或者更新
    @PostMapping
    public boolean save(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    // 根据id删除
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> list) {
        return userService.removeByIds(list);
    }

    // 获取所有数据
    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    // 根据id查询数据
    @GetMapping("/{id}")
    public User findOne(@PathVariable Integer id) {
        return userService.getById(id);
    }

    // 分页查询
    // limit第一个参数 = (pageNum - 1) * pageSize
    @GetMapping("/page")
    public Page<User> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String realname, @RequestParam(defaultValue = "") Integer sex, @RequestParam(defaultValue = "") String residence, @RequestParam(defaultValue = "") Integer deactivate, @RequestParam(defaultValue = "false") Boolean desc) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (desc) queryWrapper.orderByDesc("id"); // 是否根据id排序

        if (!"".equals(username)) queryWrapper.like("username", username);
        if (!"".equals(realname)) queryWrapper.like("realname", realname);
        if (sex != null) queryWrapper.like("sex", sex);
        if (!"".equals(residence)) queryWrapper.like("residence", residence);
        if (deactivate != null) queryWrapper.like("deactivate", deactivate);

        // 获取当前用户信息
        User currentUser = TokenUtils.getCurrenUser();
        if (currentUser != null) {
            System.out.println(" 获取当前用户信息的昵称 ============== " + currentUser.getNickname());
        }

        return userService.page(new Page<>(pageNum, pageSize), queryWrapper);
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
//        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("realname", "姓名");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("phone", "手机号");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("school", "学校");
        writer.addHeaderAlias("residence", "地址");
        writer.addHeaderAlias("introduction", "介绍");
        writer.addHeaderAlias("headPortrait", "头像");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("lastLoginTime", "最后登录");
//        writer.addHeaderAlias("language", "语言");
//        writer.addHeaderAlias("deactivate", "状态");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
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
    public boolean importFile(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

//        通过javaben 的方式读取excel内的对象 但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);
//        userService.saveBatch(list);


//        忽略标头的中文
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setRealname(row.get(2).toString());
            user.setNickname(row.get(3).toString());
            user.setSex((Integer) row.get(4));
            user.setPhone(row.get(5).toString());
            user.setEmail(row.get(6).toString());
            user.setSchool(row.get(7).toString());
            user.setResidence(row.get(8).toString());
            user.setIntroduction(row.get(9).toString());
            user.setHeadPortrait(row.get(10).toString());

            users.add(user);
        }
        userService.saveBatch(users);
        System.out.println(users);

        return true;
    }

}

