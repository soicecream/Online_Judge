package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Constants;
import com.example.demo.controller.dto.UserDto;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.entity.RoleMenu;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IMenuService;
import com.example.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2022-09-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();
    
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    //    登录用户
    @Override
    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one != null) {
            if (!one.getEnable()) throw new ServiceException(Constants.CODE_600, "用户不可用");
            else {
                BeanUtil.copyProperties(one, userDto, true); // 从数据库查询用户信息(one 复制到 userDto中)
//                设置token
                String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
                userDto.setToken(token);

                String role = one.getRole();

                // 设置用户的菜单列表
//                if(!role.equals(Constants.ROLE_USER))
                    userDto.setMenus(getRoleMenus(role));

                return userDto;
            }
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    //    注册用户
    @Override
    public User register(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDto, one, true);
            save(one); // 把copy完之后的用户对象存储到数据库中
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }


    @Override
    public Boolean updatePassword(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());

        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }

        if (one != null) {
            one.setPassword(userDto.getPassword());
            saveOrUpdate(one);
            return true;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名不存在");
        }
    }

    @Override
    public Boolean resetPassword(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());

        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }

        if (one != null) {
            one.setPassword("123456");
            saveOrUpdate(one);
            return true;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名不存在");
        }
    }

    // 从数据库查询用户信息
    private User getUserInfo(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    // 获取当前角色的菜单列表
    private List<Menu> getRoleMenus(String role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>(); queryWrapper.eq("role_key", role);
        Integer roleId = roleMapper.selectOne(queryWrapper).getId();

        QueryWrapper<RoleMenu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("role_id", roleId);
        List<Integer> menuIds = roleMenuMapper.selectList(queryWrapper1).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());

        // 查出系统所有的菜单
        List<Menu> menus = menuService.findMenus();

        // 最后筛选完之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for(Menu menu: menus) {
            if(menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf 移除children 里面不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}
