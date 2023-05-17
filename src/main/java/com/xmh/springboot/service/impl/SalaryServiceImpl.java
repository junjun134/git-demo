package com.xmh.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.controller.dto.SalaryDto;
import com.xmh.springboot.controller.dto.UserSalaryDto;
import com.xmh.springboot.entity.Salary;
import com.xmh.springboot.mapper.SalaryMapper;
import com.xmh.springboot.service.ISalaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.x509.IPAddressName;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xmh
 * @since 2023-04-26
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {


    @Resource
    private SalaryMapper salaryMapper;


    @Override
    public IPage<SalaryDto> AllSalary(Page<Object> objectPage, QueryWrapper<Salary> queryWrapper) {
        return salaryMapper.AllSalary(objectPage,queryWrapper);
    }

    @Override
    public Salary allSalary(QueryWrapper queryWrapper) {
        return salaryMapper.allSalary(queryWrapper);
    }


    @Override
    public List<Salary> listSalary() {
        return salaryMapper.listSalary();
    }
}
