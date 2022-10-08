package com.example.demo.service;

import com.example.demo.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.RoleMenu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * @since 2022-10-06
 */
public interface IRoleService extends IService<Role> {

    Boolean saveRoleMenu(Integer roleId, List<Integer> menuIds);

    List<RoleMenu> getRoleMenu(Integer roleId);
}
