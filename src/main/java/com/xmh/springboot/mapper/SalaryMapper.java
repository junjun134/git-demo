package com.xmh.springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.controller.dto.SalaryDto;
import com.xmh.springboot.controller.dto.UserSalaryDto;
import com.xmh.springboot.entity.Salary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xmh
 * @since 2023-04-26
 */
public interface SalaryMapper extends BaseMapper<Salary> {

    @Select("SELECT su.avatar_url,su.nickname,su.username,su.gender,sd.dept_name AS deptName,sc.id,sc.baseSalary,sc.bonus,sc.insure,sc.fine,sc.endSalary\n" +
            "FROM sys_user su LEFT JOIN sys_dept sd ON su.deptId = sd.id LEFT JOIN salary as sc on su.username = sc.username ${ew.customSqlSegment}")
    IPage<SalaryDto> AllSalary(Page<Object> objectPage, @Param("ew") QueryWrapper<Salary> queryWrapper);

    @Select("SELECT su.nickname as nickname,su.username,sc.id,sc.baseSalary,sc.bonus,sc.insure,sc.fine,sc.endSalary\n" +
            "FROM sys_user su LEFT JOIN salary sc on su.username = sc.username ")
    List<Salary>  listSalary();

    @Select("SELECT sc.id,sc.baseSalary,sc.bonus,sc.insure,sc.fine,sc.endSalary\n" +
            "FROM  salary sc  ${ew.customSqlSegment} ")
    Salary  allSalary(@Param("ew") QueryWrapper<Salary> queryWrapper);
}
