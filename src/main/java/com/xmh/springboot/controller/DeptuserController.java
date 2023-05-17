package com.xmh.springboot.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.common.Result;
import com.xmh.springboot.common.RoleEnum;
import com.xmh.springboot.controller.dto.DeptUserDto;
import com.xmh.springboot.entity.User;
import com.xmh.springboot.service.IUserService;
import com.xmh.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.xmh.springboot.service.IDeptuserService;
import com.xmh.springboot.entity.Deptuser;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xmh
 * @since 2023-04-22
 */
@RestController
@RequestMapping("/todept")
public class DeptuserController {

    @Resource
    private IDeptuserService deptuserService;
    @Resource
    private IUserService userService;
    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody DeptUserDto deptUserDto) throws ParseException {
        Deptuser deptuser = new Deptuser();
//        if (deptUserDto.getId() == null) {
//            // 新增
//            deptuser.setTime(DateUtil.parse(DateUtil.today()));
//        }
        if(deptUserDto.getDeptId()!=deptUserDto.getNewDept() && deptUserDto.getNewDept()!=null && "审核通过".equals(deptUserDto.getState())){
            User user = new User();
            user.setId(Integer.parseInt(deptUserDto.getUserId()));
            user.setDeptId(deptUserDto.getNewDept());
            UpdateWrapper<User> userUpdateWrapper= new UpdateWrapper<>();
            userUpdateWrapper.set("deptId",deptUserDto.getNewDept()).eq("id",Integer.parseInt(deptUserDto.getUserId()));
            userService.update(userUpdateWrapper);
        }
        BeanUtil.copyProperties(deptUserDto,deptuser);
        deptuserService.saveOrUpdate(deptuser);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        deptuserService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        deptuserService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(deptuserService.findAll());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(deptuserService.getById(id));
    }

//    @GetMapping("/page2")
//    public Result list(@RequestParam(defaultValue = "1") Integer pageNum,
//                       @RequestParam(defaultValue = "10") Integer pageSize,
//                        @RequestParam Integer id,
//                        @RequestParam(defaultValue = "") String name
////                       @RequestBody User user
//    ){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        User user = userService.getOneUser(queryWrapper.eq("id",id));
//        if (RoleEnum.ROLE_common.toString().equals(user.getRole())) {  // 角色是普通用户
//            queryWrapper.eq("sdu.userId", user.getId());
//        }
//    return Result.success(deptuserService.selectUserPageMap(new Page(pageNum,pageSize),queryWrapper));
//    }
    @GetMapping("/page2")
    public Result list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam Integer id,
                       @RequestParam(defaultValue = "") String name
//                         @RequestBody User user
    ){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        User user = userService.getOneUser(queryWrapper1.eq("su.id",id));
        if (RoleEnum.ROLE_common.toString().equals(user.getRole())) {  // 角色是普通用户
            queryWrapper.eq("sdu.userId", user.getId());
        }
        if(!"".equals(name)){
            queryWrapper.eq("su.nickname",name);
        }
        return Result.success(deptuserService.selectUserPageMap(new Page(pageNum,pageSize),queryWrapper));
    }
}

