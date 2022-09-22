package com.example.demo.entity;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String realname;
    private String nickname;
    private String email;
    private Integer sex;
    private String school;
    private String residence;
    private String introduction;
    private String head_portrait;
    private String create_time;
    private String last_login_time;
    private Integer language;
    private Integer deactivate;

}
