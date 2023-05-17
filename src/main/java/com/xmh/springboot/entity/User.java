package com.xmh.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User对象", description = "")
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    @ApiModelProperty("用户名")
    private String username;

    @TableField("password")
    @ApiModelProperty("密码")
    private String password;

    @TableField("nickname")
    @ApiModelProperty("昵称")
    private String nickname;

    @TableField("gender")
    @ApiModelProperty("性别 0男 1女  默认0")
    private Integer gender;

    @TableField("deptId")
    @ApiModelProperty("部门id")
    private Integer deptId;

    @TableField(exist = false)
    @ApiModelProperty("部门名称")
    private String deptName;

    @TableField("email")
    @ApiModelProperty("邮箱")
    private String email;

    @TableField("phone")
    @ApiModelProperty("电话")
    private String phone;

    @TableField("address")
    @ApiModelProperty("地址")
    private String address;

    @TableField("create_time")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("join_time")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date joinTime;

    @TableField("avatar_url")
    @ApiModelProperty("头像")
    private String avatarUrl;

    @TableField("role")
    @ApiModelProperty("角色")
    private String role;

    @TableField(exist = false)
    @ApiModelProperty("角色")
    private String roleName;


}
