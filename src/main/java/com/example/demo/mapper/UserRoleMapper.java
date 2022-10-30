package com.example.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2022-10-29
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    Page<UserRole> findPage(Page<UserRole> page, String userUsername, String userRealname, Integer roleId, String desc);
}
