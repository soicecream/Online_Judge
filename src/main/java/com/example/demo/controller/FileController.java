package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Files;
import com.example.demo.entity.User;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.IFileService;
import com.example.demo.utils.TokenUtils;
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
 * 文件上传相关接口
 * </p>
 *
 * @author 作者
 * @since 2022-09-29
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private IFileService fileService;

    /**
     * 文件上传接口
     *
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUuid = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUuid);
        // 判断配置文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        // 把获取到的文件存储到磁盘目录
        file.transferTo(uploadFile);
        // 获取md5
        String md5 = SecureUtil.md5(uploadFile);
        //  从数据库查询是否有相同的记录
        Files dbFiles = getFileByMd5(md5);

        // 获取文件的url
        String url;
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            // 由于文件已存在， 所有删除刚才上传的重复文件
            uploadFile.delete();
        } else {
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/" + fileUuid;
        }

        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024); // B => KB
        saveFile.setUrl(url);
        saveFile.setMd5(md5);


        fileService.saveOrUpdate(saveFile);

        return url;
    }

    /**
     * 文件下载接口
     * 导出通过流导出到网页中
     *
     * @param fileUuid
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码 获取文件
        File uploadFile = new File(fileUploadPath + fileUuid);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUuid, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取上传的字节流
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
        List<Files> fileList = fileService.list(queryWrapper);
        return fileList.size() == 0 ? null : fileList.get(0);
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Files files) {
        return Result.success(fileService.saveOrUpdate(files));
    }

    @PostMapping("/updateBatch")
    public Result updateBatch(@RequestBody Files files) {
        return Result.success(fileService.saveOrUpdate(files));
    }

    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(fileService.removeById(id));
    }

    // 以id来删除多条数据     数据是 [1, 2, 3] 这样的
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(fileService.removeByIds(list));
    }


    // 分页查询
    // limit第一个参数 = (pageNum - 1) * pageSize
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") Integer enable,
                           @RequestParam(defaultValue = "false") Boolean desc) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        if (desc) queryWrapper.orderByDesc("id"); // 是否根据id排序

        if (!"".equals(name)) queryWrapper.like("name", name);
        if (enable != null) queryWrapper.like("enable", enable);

        return Result.success(fileService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


}

