package com.xmh.springboot.controller.dto;

import lombok.Data;
@Data
public class DeptDto {

    private Integer id;

    private String deptCode;

    private String deptName;

    private String remark;

    private Integer parentId;

}
