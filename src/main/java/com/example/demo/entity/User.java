package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 作者
 * @since 2022-09-25
 */
@Getter
@Setter
@TableName("sys_user")
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

    @ApiModelProperty("姓名")
    private String realname;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别 (0男，1女)")
    private Integer sex;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("介绍")
    private String introduction;

    @ApiModelProperty("头像")
    private String headPortrait;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //是前端往后段传的时候使用
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") //后端往前端传输的时候使用
    private Date createTime;

    @ApiModelProperty("语言")
    private Integer language;

    @ApiModelProperty("是否参加排名 (1参加， 0不参加)")
    private Boolean isRank;

    @ApiModelProperty("状态 (1启用，0禁用)")
    private Boolean enable;

    @ApiModelProperty("假删除 (1删除，0未删除)")
    private Boolean isDelete;

    // 该字段在数据库中是没有的
    @TableField(exist = false)
    private String token;

    // 该字段在数据库中是没有的
    @TableField(exist = false)
    private String UserListText;

    // 该字段在数据库中是没有的
    @TableField(exist = false)
    private List<Menu> menus;

}
