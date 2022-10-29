package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2022-10-29
 */
@Getter
@Setter
  @TableName("sys_user_role")
@ApiModel(value = "UserRole对象", description = "")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("用户id")
        private Integer userid;

      @ApiModelProperty("用户的用户名")
      private String userUsername;

      @ApiModelProperty("权限")
      private String role;


}
