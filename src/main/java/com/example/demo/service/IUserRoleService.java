package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * @since 2022-10-29
 */
public interface IUserRoleService extends IService<UserRole> {

    Boolean increase(UserRole userRole);

    Boolean updateRole(UserRole userRole);

    Page<UserRole> findPage(Page<UserRole> Page, String userUsername, String userRealname, Integer roleId, Boolean desc);
}
