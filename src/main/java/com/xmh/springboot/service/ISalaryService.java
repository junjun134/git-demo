package com.xmh.springboot.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.controller.dto.SalaryDto;
import com.xmh.springboot.controller.dto.UserSalaryDto;
import com.xmh.springboot.entity.Salary;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xmh
 * @since 2023-04-26
 */
public interface ISalaryService extends IService<Salary> {

    IPage<SalaryDto> AllSalary(Page<Object> objectPage, QueryWrapper<Salary> queryWrapper);

    Salary  allSalary(QueryWrapper queryWrapper);

    List<Salary>  listSalary();
}
