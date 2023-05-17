package com.xmh.springboot.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.controller.dto.UserDTO;
import com.xmh.springboot.controller.dto.UserPasswordDTO;
import com.xmh.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>

 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User getOneUser(Wrapper<User> queryWrapper);

    User getOneUser2(Wrapper<User> queryWrapper);

    List<User> findAllUser();

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Page<User> findPage(Page<User> objectPage, QueryWrapper queryWrapper);
}
