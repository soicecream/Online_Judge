package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.Files;
import com.example.demo.service.IFilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/files")
public class FilesController {

    @Resource
    private IFilesService filesService;

    @Value("${files.upload.path}")
    private String fileUploadPath;

//        @Value("${server.ip}")
//        private String serverIp;


    // 分页查询接口
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") Integer enable, @RequestParam(defaultValue = "false") Boolean desc) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        if (desc) queryWrapper.orderByDesc("id"); // 是否根据id排序

        if (!"".equals(name)) queryWrapper.like("name", name);
        if (!"".equals(type)) queryWrapper.like("type", type);
        if (enable != null) queryWrapper.eq("enable", enable);

        return Result.success(filesService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Files files) {
        return Result.success(filesService.saveOrUpdate(files));
    }

    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(filesService.removeById(id));
    }

    // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(filesService.removeByIds(list));
    }


    /**
     * 文件上传接口
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/import/file")
    public Result upload( MultipartFile file) throws IOException {
        System.out.println("file  " + file);

        if (file == null || file.isEmpty()) {
            return Result.error(Constants.CODE_400, "请选择文件");
        }

        return Result.success(file);

//        String originalFileName = file.getOriginalFilename();
//        String type = FileUtil.extName(originalFileName);
//        if(type == null) {
//            return Result.error(Constants.CODE_400, "文件类型有问题，请确认文件类型");
//        }
//        originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf(type) - 1);
//        long size = file.getSize();
//        if(size == 0) {
//            return Result.error(Constants.CODE_400, "暂无文件，请选择文件");
//        }
//
//        // 定义一个文件的唯一标识码
//        String uuid = IdUtil.fastSimpleUUID();
//        String fileUUID = uuid + StrUtil.DOT + type;
//
//        File uploadFile = new File(fileUploadPath + fileUUID);
//
//        // 判断配置的文件目录是否存在， 若不存在则创建一个新的文件目录
//        File parentFile = uploadFile.getParentFile();
//        if (!parentFile.exists()) {
//            parentFile.mkdirs();
//        }
//
//        String url;
//        // 上传文件到磁盘
//        file.transferTo(uploadFile);
//        // 获取文件的md5
//        String md5 = SecureUtil.md5(uploadFile);
//        // 从数据库查询是否存在相同的数据
//        Files dbFiles = getFileByMd5(md5);
//        if (dbFiles != null) {
//            url = dbFiles.getUrl();
//            // 由于文件已存在，所以删除刚才上传的重复文件
//            uploadFile.delete();
//        } else {
//            // 数据库若不存在重复文件，则不删除刚才上传的文件
//            url = "http://localhost:9090/files/" + fileUUID;
//        }
//
//
//        // 存储数据库
//        Files saveFile = new Files();
//        saveFile.setName(originalFileName);
//        saveFile.setType(type);
//        saveFile.setSize(size / 1024);
//        saveFile.setUrl(url);
//        saveFile.setMd5(md5);
//        filesService.saveOrUpdate(saveFile);
//
//        return Result.success(url);
    }

    /**
     * 文档下载接口 http://localhost:9090/files/{fileUUID}
     *
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
//        设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();

    }

    /**
     * 通过文件的md5查询文件
     *
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> list = filesService.list(queryWrapper);
        return list.size() == 0 ? null : list.get(0);
    }

}

