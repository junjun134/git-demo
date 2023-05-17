package com.xmh.springboot.controller.dto;

import com.xmh.springboot.entity.Menu;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class UserSalaryDto {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private Integer gender;
    private Integer deptId;
    private String deptName;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinTime;
    private String email;
    private String phone;
    private String avatarUrl;
    private String token;
    private String roleName;
    private String role;
    private List<Menu> menus;
    private Integer salaryId;
    private Float baseSalary;//基本薪资
    private Float bonus;//奖金
    private Float insure;//保险扣款
    private Float fine;//处罚金额
    private Float endSalary;//实发薪资

}
