package com.xmh.springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.controller.dto.UserPasswordDTO;
import com.xmh.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>

 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    @Select("SELECT su.*,sr.name as roleName,sd.dept_name AS deptName,sd.id AS deptId FROM sys_user su LEFT JOIN sys_dept sd ON su.deptId = sd.id LEFT JOIN sys_role sr on sr.code = su.role ${ew.customSqlSegment}")
    Page<User> findPage(Page<User> page, @Param("ew")Wrapper<User> queryWrapper);

    @Select("SELECT su.*,sr.code as role,sr.name as roleName,sd.dept_name AS deptName,sd.id AS deptId FROM sys_user su LEFT JOIN sys_dept sd ON su.deptId = sd.id LEFT JOIN sys_role sr on su.role = sr.code")
    List<User> findAllUser();

    @Select("SELECT su.*,sr.name as roleName,sd.dept_name AS deptName,sd.id AS deptId FROM sys_user su LEFT JOIN sys_dept sd ON su.deptId = sd.id LEFT JOIN sys_role sr on sr.code = su.role ${ew.customSqlSegment}")
    User getOneUser(@Param("ew")Wrapper<User> queryWrapper);

    @Select("SELECT su.*,sr.name as roleName,sd.dept_name AS deptName,sd.id AS deptId,sc.id as salaryId,sc.baseSalary,sc.bonus,sc.insure,sc.fine,sc.endSalary  FROM sys_user su LEFT JOIN sys_dept sd ON su.deptId = sd.id LEFT JOIN sys_role sr on sr.code = su.role LEFT JOIN salary as sc on su.username = sc.username ${ew.customSqlSegment}")
    User getOneUser2(@Param("ew")Wrapper<User> queryWrapper);


}
