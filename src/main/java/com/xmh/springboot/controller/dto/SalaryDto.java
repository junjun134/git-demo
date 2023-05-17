package com.xmh.springboot.controller.dto;

import lombok.Data;

@Data
public class SalaryDto {
    private Integer id;
    private String username;
    private String deptName;
    private String avatarUrl;
    private String nickname;
    private Integer gender;
    private Float baseSalary;//基本薪资
    private Float bonus;//奖金
    private Float insure;//保险扣款
    private Float fine;//处罚金额
    private Float endSalary;//实发薪资
}
