package com.xmh.springboot.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xmh.springboot.controller.dto.DeptUserDto;
import com.xmh.springboot.entity.Deptuser;
import com.xmh.springboot.entity.User;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xmh
 * @since 2023-04-22
 */
public interface IDeptuserService extends IService<Deptuser> {

    List<DeptUserDto> findAll();

    IPage<Map<String, Object>> selectUserPageMap(Page page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);

}
