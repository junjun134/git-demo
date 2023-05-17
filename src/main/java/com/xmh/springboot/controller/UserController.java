package com.xmh.springboot.controller;


import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.common.Constants;
import com.xmh.springboot.common.Result;
import com.xmh.springboot.controller.dto.UserDTO;
import com.xmh.springboot.controller.dto.UserPasswordDTO;
import com.xmh.springboot.entity.Dept;
import com.xmh.springboot.entity.Role;
import com.xmh.springboot.entity.Salary;
import com.xmh.springboot.entity.User;
import com.xmh.springboot.service.IDeptService;
import com.xmh.springboot.service.IRoleService;
import com.xmh.springboot.service.ISalaryService;
import com.xmh.springboot.service.IUserService;
import com.xmh.springboot.utils.AdaptiveWidthUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private IDeptService deptService;
    @Resource
    private ISalaryService salaryService;
    @Resource
    private IRoleService roleService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        try {
            UserDTO dto = userService.login(userDTO);
            return Result.success(dto);
        }catch (Exception e){
            return Result.error("账号或者密码错误");
        }
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody UserDTO userDTO) {
        User user =new User();
        BeanUtil.copyProperties(userDTO, user);
        if (userDTO.getId() == null && userDTO.getPassword() == null) {  // 新增用户默认密码
            user.setPassword( SecureUtil.md5("123"));
            user.setRole("ROLE_common");
            user.setAvatarUrl("http://localhost:9090/file/831646943d984cb79c06401c2191b194.jpeg");
        }
        try {
            return Result.success(userService.saveOrUpdate(user));
        }catch (Exception e){
            return Result.error("工号已经存在");
        }

    }

    /**
     * 修改密码
     * @param userPasswordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    @Transactional
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        QueryWrapper queryWrapper =new QueryWrapper();
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper.eq("id",id);
        queryWrapper1.eq("username",userService.getOne(queryWrapper).getUsername());
        salaryService.remove(queryWrapper1);
        return Result.success(userService.removeById(id));
    }

    @Transactional
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {

        List<Integer> ids1 = new ArrayList<>();
        for (Integer id:ids) {
            QueryWrapper<Salary> queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("username",userService.getById(id).getUsername());
            ids1.add(salaryService.allSalary(queryWrapper1).getId());
        }
        System.out.println(ids1);
        salaryService.removeByIds(ids1);
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.findAllUser());
    }

    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        List<User> list = userService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOneUser(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String searchname,
                           @RequestParam(defaultValue = "") String searchphone,
                           @RequestParam(defaultValue = "") String searchId
    )  {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(searchname)) {
            queryWrapper.like("nickname", searchname);
        }
        if (!"".equals(searchphone)) {
            queryWrapper.like("phone", searchphone);
        }
        if (!"".equals(searchId)) {
            queryWrapper.like("sd.id", searchId);
        }
        return Result.success(userService.findPage(new Page<>(pageNum, pageSize),queryWrapper));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.findAllUser();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("gender", "性别");
        writer.addHeaderAlias("deptName", "部门名称");
        writer.addHeaderAlias("roleName","职位");
        writer.addHeaderAlias("joinTime","入职日期");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("avatarUrl", "头像");
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置自适应列宽
        AdaptiveWidthUtils.setSizeColumn(writer.getSheet(), 11);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
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
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

//        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            user.setUsername(row.get(0).toString());
//            user.setPassword(row.get(1).toString());
//            user.setNickname(row.get(2).toString());
//            user.setGender(Convert.toInt(row.get(3).toString()));
//            user.setDeptId(Convert.toInt(row.get(4).toString()));
//            user.setEmail(row.get(5).toString());
//            user.setPhone(row.get(6).toString());
//            user.setAddress(row.get(7).toString());
//            user.setAvatarUrl(row.get(8).toString());
//            users.add(user);
//        }

        //方法三
        reader.addHeaderAlias("用户名", "username");
        reader.addHeaderAlias("昵称","nickname");
        reader.addHeaderAlias("性别","gender");
        reader.addHeaderAlias("部门名称","deptName");
        reader.addHeaderAlias("入职日期","joinTime");
        reader.addHeaderAlias("邮箱","email");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("地址","address");
        reader.addHeaderAlias("头像","avatarUrl");
        List<User> users = reader.readAll(User.class);
        for (User user: users) {
            QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dept_name", user.getDeptName());
            QueryWrapper<Role> queryWrapper1 = new QueryWrapper<>();
            Dept dept = this.deptService.getOne(queryWrapper);
            user.setDeptId(dept.getId());
            user.setPassword( SecureUtil.md5("123"));
            user.setRole("ROLE_common");
            user.setAvatarUrl("http://localhost:9090/file/831646943d984cb79c06401c2191b194.jpeg");
            try {
                userService.save(user);
            }catch (Exception e){
                return Result.error("工号:"+user.getUsername()+"    姓名:"+user.getNickname());
            }
        }
        return Result.success();

    }

}

