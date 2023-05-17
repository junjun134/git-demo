package com.xmh.springboot.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.common.Result;
import com.xmh.springboot.controller.dto.DeptDto;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.xmh.springboot.service.IDeptService;
import com.xmh.springboot.entity.Dept;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author xmh
 * @since 2023-04-19
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Resource
    private IDeptService deptService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody DeptDto dept) {
        Dept dept1 = new Dept();
        BeanUtil.copyProperties(dept,dept1);
        deptService.saveOrUpdate(dept1);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        deptService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        deptService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(deptService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(deptService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(     @RequestParam String name,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("dept_name",name);
        return Result.success(deptService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

