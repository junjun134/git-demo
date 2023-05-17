package com.xmh.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author xmh
 * @since 2023-04-19
 */
@Getter
@Setter
@TableName("sys_dept")
@ApiModel(value = "Dept对象", description = "部门表")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("部门id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @TableField("dept_code")
      @ApiModelProperty("部门编码")
      private String deptCode;

      @TableField("dept_name")
      @ApiModelProperty("部门名称")
      private String deptName;

      @TableField("remark")
      @ApiModelProperty("部门备注")
      private String remark;

      @TableField("parent_id")
      @ApiModelProperty("父级部门id，0根部门")
      private Integer parentId;

      @TableField("create_time")
      @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
      @ApiModelProperty("创建时间")
      private LocalDateTime createTime;



}
