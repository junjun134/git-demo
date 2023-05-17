package com.xmh.springboot.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author xmh
 * @since 2023-04-26
 */
@Getter
@Setter
  @ApiModel(value = "Salary对象", description = "")
@TableName("salary")
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id",type = IdType.AUTO)
      private Integer id;

      @TableField("username")
      @ApiModelProperty("员工账号")
      private String username;

      @TableField("baseSalary")
      @ApiModelProperty("基本工资")
      private Float baseSalary;

      @TableField("bonus")
      @ApiModelProperty("奖金")
      private Float bonus;

      @TableField(exist = false)
      private String nickname;

      @TableField("insure")
      @ApiModelProperty("五险一金")
      private Float insure;

      @TableField("fine")
      @ApiModelProperty("罚款")
      private Float fine;

      @TableField("endSalary")
      @ApiModelProperty("最终薪资")
      private Float endSalary;


}
