package com.xmh.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2023-04-22
 */
@Getter
@Setter
  @TableName("sys_deptuser")
@ApiModel(value = "Deptuser对象", description = "")
public class Deptuser implements Serializable {

      @TableId(value = "id",type = IdType.AUTO)
      @ApiModelProperty("id")
        private Integer id;

      @TableField("userId")
      @ApiModelProperty("员工id")
      private String userId;

      @TableField(exist = false)
      @ApiModelProperty("员工姓名")
      private String nickname;

      @TableField("time")
      @ApiModelProperty("申请时间")
      private Date time;

      @TableField("old_dept")
      @ApiModelProperty("原本部门id")
      private Integer deptId;

      @TableField(exist = false)
      @ApiModelProperty("老部门名称")
      private String deptName;

      @TableField("new_dept")
      @ApiModelProperty("新部门id")
      private Integer newDept;

      @TableField("state")
      @ApiModelProperty("状态")
      private String state;


}
