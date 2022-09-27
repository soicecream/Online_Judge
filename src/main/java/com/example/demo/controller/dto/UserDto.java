package com.example.demo.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 接受前端登录请求的参数
 */

@Data
public class UserDto {

    private Integer id;
    private String username; // 用户名
    private String password; // 密码
    private String nickname; // 昵称
    private String realname; // 姓名
    private Integer sex; // 性别
    private String phone; // 电话
    private String email; // 邮箱
    private String school; // 学校
    private String residence; // 地址
    private String introduction; // 自我介绍
    private String headPortrait; // 头像
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime lastLoginTime; // 最后登录
    private Integer language; // 语言
    private Integer deactivate; // 状态


}
