package com.example.demo.controller.dto;

import lombok.Data;

/**
 * 接受前端登录请求的参数
 */

@Data
public class UserDto {
    private String username; // 用户名
    private String password; // 密码
    private String nickname; // 昵称
    private String head_portrait; // 头像


}
