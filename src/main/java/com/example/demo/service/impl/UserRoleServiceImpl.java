package com.example.demo.service.impl;

import cn.hutool.log.Log;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRoleMapper;
import com.example.demo.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2022-10-29
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Page<UserRole> findPage(Page<UserRole> Page, String userUsername, String userRealname, Integer roleId, Boolean desc) {
        return userRoleMapper.findPage(Page, userUsername, userRealname, roleId, desc ? "desc" : "asc");
    }

    // 添加用户权限
    @Override
    public Boolean increase(UserRole userRole) {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_username", userRole.getUserUsername());
        List<UserRole> userRoleList = userRoleMapper.selectList(userRoleQueryWrapper);
        if (userRoleList != null && userRoleList.size() > 0) {
            throw new ServiceException(Constants.CODE_400, "用户已拥有权限");
        }

        QueryWrapper<User> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("username", userRole.getUserUsername());
        List<User> list = userMapper.selectList(userqueryWrapper);
        if (list.size() == 1) {
            userRole.setUserRealname(list.get(0).getRealname());
            save(userRole);
            return true;
        }
        if (list.size() == 0) {
            throw new ServiceException(Constants.CODE_400, "用户不存在");
        } else {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
    }

    // 修改用户权限
    public Boolean updateRole(UserRole userRole) {

        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_username", userRole.getUserUsername());

        if (userRole.getRoleId() == 3) {
            userRoleMapper.delete(queryWrapper);
            return true;
        }

        saveOrUpdate(userRole);
        return true;
    }


}
