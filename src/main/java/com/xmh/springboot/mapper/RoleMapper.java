package com.xmh.springboot.mapper;

import com.xmh.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>

 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where code = #{code}")
    Integer selectByFlag(@Param("code") String code);
}
