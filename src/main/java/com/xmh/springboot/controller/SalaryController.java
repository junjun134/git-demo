package com.xmh.springboot.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.common.Result;
import com.xmh.springboot.controller.dto.SalaryDto;
import com.xmh.springboot.controller.dto.UserSalaryDto;
import com.xmh.springboot.entity.Dept;
import com.xmh.springboot.entity.User;
import com.xmh.springboot.utils.AdaptiveWidthUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.xmh.springboot.service.ISalaryService;
import com.xmh.springboot.entity.Salary;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xmh
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Resource
    private ISalaryService salaryService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Salary salary) {
        Float end = salary.getBaseSalary() + salary.getBonus() - salary.getInsure() - salary.getFine();
        salary.setEndSalary(end);
        salaryService.saveOrUpdate(salary);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        salaryService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        salaryService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(salaryService.listSalary());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(salaryService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findOneSalary(@PathVariable("username") String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        return Result.success(salaryService.getOne(queryWrapper));
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "1")Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String searchname,
                           @RequestParam(defaultValue = "") String searchId

    ) {
        QueryWrapper<Salary> queryWrapper = new QueryWrapper<>();
        if (!"".equals(searchname)) {
            queryWrapper.like("nickname", searchname);
        }
        if (!"".equals(searchId)) {
            queryWrapper.like("sd.id", searchId);
        }
        IPage<SalaryDto> page = salaryService.AllSalary(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Salary> list = salaryService.listSalary();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "工号");
        writer.addHeaderAlias("nickname", "姓名");
        writer.addHeaderAlias("baseSalary", "基础薪资");
        writer.addHeaderAlias("bonus", "奖金");
        writer.addHeaderAlias("insure", "五险一金");
        writer.addHeaderAlias("fine", "罚款");
        writer.addHeaderAlias("endSalary", "最终薪资");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置自适应列宽
        AdaptiveWidthUtils.setSizeColumn(writer.getSheet(), 6);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("薪资表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方法三
        reader.addHeaderAlias("工号", "username");
        reader.addHeaderAlias("姓名", "nickname");
        reader.addHeaderAlias("基础薪资", "baseSalary");
        reader.addHeaderAlias("奖金", "bonus");
        reader.addHeaderAlias("五险一金", "insure");
        reader.addHeaderAlias("罚款", "fine");
        reader.addHeaderAlias("最终薪资", "endSalary");
        List<Salary> lists =   reader.readAll(Salary.class);
        for (Salary salary: lists) {
            try{
                salaryService.save(salary);
            }catch (Exception e){
                return Result.error("工号: "+salary.getNickname()+"   姓名:"+salary.getNickname());
            }
        }
        return Result.success();


    }


}

