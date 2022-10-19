package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 题目
 * </p>
 *
 * @author 作者
 * @since 2022-10-14
 */
@Getter
@Setter
  @TableName("sys_problem")
@ApiModel(value = "Problem对象", description = "题目")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("标题")
      private String title;

      @ApiModelProperty("作者")
      private String author;

      @ApiModelProperty("类型")
      private Integer type;

      @ApiModelProperty("时间限制(ms)，默认为c/c++限制,其它语言为2倍")
      private Integer timeLimit;

      @ApiModelProperty("空间限制(mb)，默认为c/c++限制,其它语言为2倍")
      private Integer memoryLimit;

      @ApiModelProperty("栈限制(mb)，默认为128")
      private Integer stackLimit;

      @ApiModelProperty("内容描述")
      private String description;

      @ApiModelProperty("输入描述")
      private String input;

      @ApiModelProperty("输出描述")
      private String output;

      @ApiModelProperty("难度")
      private Integer difficulty;

      @ApiModelProperty("备注")
      private String hint;

      @ApiModelProperty("状态")
      private Boolean auth;

      @ApiModelProperty("创建时间")
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //是前端往后段传的时候使用
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") //后端往前端传输的时候使用
      private LocalDateTime createTime;

      @ApiModelProperty("修改时间")
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //是前端往后段传的时候使用
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") //后端往前端传输的时候使用
      private LocalDateTime updateTime;


}
