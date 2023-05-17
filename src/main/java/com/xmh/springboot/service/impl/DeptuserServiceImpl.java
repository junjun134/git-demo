package com.xmh.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmh.springboot.controller.dto.DeptUserDto;
import com.xmh.springboot.entity.Deptuser;
import com.xmh.springboot.entity.User;
import com.xmh.springboot.mapper.DeptuserMapper;
import com.xmh.springboot.service.IDeptuserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xmh
 * @since 2023-04-22
 */
@Service
public class DeptuserServiceImpl extends ServiceImpl<DeptuserMapper, Deptuser> implements IDeptuserService {

    @Resource
    private DeptuserMapper deptuserMapper;
    @Override
    public List<DeptUserDto> findAll() {
        return deptuserMapper.findAll();
    }

    @Override
    public IPage<Map<String, Object>> selectUserPageMap(Page page, Wrapper<User> queryWrapper) {
        return deptuserMapper.selectUserPageMap(page,queryWrapper);
    }


}
