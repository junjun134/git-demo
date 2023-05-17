package com.xmh.springboot.service.impl;

import com.xmh.springboot.entity.Dept;
import com.xmh.springboot.mapper.DeptMapper;
import com.xmh.springboot.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author xmh
 * @since 2023-04-19
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

}
