package com.xmh.springboot.controller.dto;

import com.xmh.springboot.entity.Menu;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private Integer gender;
    private Integer deptId;
    private String deptName;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinTime;
    private String email;
    private String phone;
    private String avatarUrl;
    private String token;
    private String roleName;
    private String role;
    private List<Menu> menus;
}
