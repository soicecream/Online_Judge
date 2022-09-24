package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 作者
 * @since 2022-09-24
 */
@Getter
@Setter
  @ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("用户名")
      private String username;

      @ApiModelProperty("密码")
      private String password;

      @ApiModelProperty("真实姓名")
      private String realname;

      @ApiModelProperty("昵称")
      private String nickname;

      @ApiModelProperty("手机号")
      private String phone;

      @ApiModelProperty("邮箱")
      private String email;

      @ApiModelProperty("性别 (0男，1女)")
      private Integer sex;

      @ApiModelProperty("学校")
      private String school;

      @ApiModelProperty("居住地")
      private String residence;

      @ApiModelProperty("介绍")
      private String introduction;

      @ApiModelProperty("头像")
      private String headPortrait;

      @ApiModelProperty("创建时间")
      private LocalDateTime createTime;

      @ApiModelProperty("最后登录")
      private LocalDateTime lastLoginTime;

      @ApiModelProperty("语言")
      private Integer language;

      @ApiModelProperty("禁用 (0不停，1停)")
      private Integer deactivate;


}
