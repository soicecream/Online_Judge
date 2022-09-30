package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-09-29
 */
@Getter
@Setter
  @ApiModel(value = "File对象", description = "")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("文件名称")
      private String name;

      @ApiModelProperty("文件类型")
      private String type;

      @ApiModelProperty("文件大小")
      private Long size;

      @ApiModelProperty("下载链接")
      private String url;

      @ApiModelProperty("是否删除(0未删除 1删除)")
      private Integer isDelete;

      @ApiModelProperty("是否禁用(0禁用 1启用)")
      private Integer enable;


}
