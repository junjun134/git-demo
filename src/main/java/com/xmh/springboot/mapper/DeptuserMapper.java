package com.xmh.springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.controller.dto.DeptUserDto;
import com.xmh.springboot.entity.Deptuser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmh.springboot.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xmh
 * @since 2023-04-22
 */
public interface DeptuserMapper extends BaseMapper<Deptuser> {
    @Select("select sdu.id,sdu.userId,su.nickname,sd.dept_name,sdu.time,su.deptId,sdu.new_dept,sdu.state\n" +
            "from sys_deptuser sdu left join sys_user su on sdu.userId = su.id LEFT JOIN sys_dept sd on sdu.old_dept = sd.id")
    List<DeptUserDto> findAll();

    @Select("select sdu.id,sdu.userId,su.nickname,sd.dept_name,sdu.time,su.deptId,sdu.new_dept,sdu.state\n" +
            "from sys_deptuser sdu left join sys_user su on sdu.userId = su.id LEFT JOIN sys_dept sd on sdu.old_dept = sd.id\n" +
            "where sdu.userId= ${id};")
    List<DeptUserDto> list(@Param("id")Integer id);

    @Select("select sdu.id,sdu.userId,su.nickname,sd.dept_name,sdu.time,su.deptId,sdu.new_dept as newDept,sdu.state\n" +
            "from sys_deptuser sdu left join sys_user su on sdu.userId = su.id LEFT JOIN sys_dept sd on sdu.old_dept = sd.id\n" +
            "${ew.customSqlSegment}")
    IPage<Map<String, Object>> selectUserPageMap(Page page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);
}
