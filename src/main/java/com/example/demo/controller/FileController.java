package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传相关接口
 */

@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping
    public String upload(@RequestParam MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        //先存储到磁盘

        // 存储数据库

        return "";
    }

}
