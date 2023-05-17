package com.xmh.springboot.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DeptUserDto {

    private Integer id;

    private String userId;

    private String nickname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    private Integer deptId;

    private Integer newDept;

    private String state;
}
