package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Constants;
import com.example.demo.entity.Menu;
import com.example.demo.entity.RoleMenu;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRoleMapper;
import com.example.demo.service.IMenuService;
import com.example.demo.service.IUserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
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
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    private UserRoleMapper userRoleMapper;

    //    登录用户
    @Override
    public User login(User userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }

        User one = getUserInfoUsername(username);
        if (one != null && one.getPassword().equals(password)) {
            if (!one.getEnable()) {
                throw new ServiceException(Constants.CODE_600, "用户不可用");
            } else {
                BeanUtil.copyProperties(one, userDto, true); // 从数据库查询用户信息(one 复制到 userDto中)
//                设置token
                String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
                userDto.setToken(token);

                // 设置管理员的路由
                UserRole userRole = getUserRoleUsername(username);
                if (userRole != null) {
                    userDto.setMenus(getRoleMenus(userRole.getRoleId()));
                }

                return userDto;
            }
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    //    注册用户
    @Override
    public Boolean register(User userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }

        User one = getUserInfoUsername(username);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDto, one, true);
            save(one); // 把copy完之后的用户对象存储到数据库中
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return true;
    }

    // 修改密码
    @Override
    public Boolean updatePassword(User userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }

        User one = getUserInfoUsername(username);
        if (one != null && one.getPassword().equals(password)) {
            one.setPassword(userDto.getPassword());
            saveOrUpdate(one);
            return true;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名不存在");
        }
    }

    // 重置密码
    @Override
    public Boolean resetPassword(User userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }

        User one = getUserInfoUsername(username);
        if (one != null) {
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_username", one.getUsername());
            UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
            if (userRole != null && userRole.getRoleId() == 1) {
                throw new ServiceException(Constants.CODE_600, "不能修改超级管理员密码");
            }

            one.setPassword(userDto.getPassword());
            updateById(one);
            return true;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名不存在");
        }
    }

    // 添加一个用户
    @Override
    public Boolean addOneUser(User userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new ServiceException(Constants.CODE_400, "参数错误");
        }

        User one = getUserInfoUsername(username);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDto, one, true);
            if (StrUtil.isBlank(one.getNickname()))
                one.setNickname(userDto.getUsername());
            if (StrUtil.isBlank(one.getNickname()))
                one.setNickname(userDto.getUsername());
            save(one);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }

        return true;
    }

    // 添加用户列表
    @Override
    public Boolean addListUser(Collection<User> userDtoList) {
        List<User> list = new ArrayList<>();

        for (User i : userDtoList) {
            User check = getUserInfoUsername(i.getUsername());
            if (check != null) {
                continue;
            }

            User user = new User();
            BeanUtil.copyProperties(i, user, true);
            list.add(user);
        }
        saveBatch(list);

        return true;
    }

    // 删除一个用户
    @Override
    public Boolean MyRemoveById(Integer id) {
        User userInfo = getUserInfoId(id);
        if (userInfo == null) {
            throw new ServiceException(Constants.CODE_600, "用户不存在");
        }

        UserRole userRole = getUserRoleUsername(userInfo.getUsername());
        if (userRole != null) {
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_username", userRole.getUserUsername());
            userRoleMapper.delete(userRoleQueryWrapper);
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", id);
        remove(userQueryWrapper);

        return true;
    }

    // 删除多个用户
    @Override
    public Boolean MyRemoveByIds(List<Integer> list) {
        for (int id : list) {
            User userInfo = getUserInfoId(id);
            if (userInfo == null) {
                continue;
            }

            UserRole userRole = getUserRoleUsername(userInfo.getUsername());
            if (userRole != null) {
                QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("user_username", userRole.getUserUsername());
                userRoleMapper.delete(userRoleQueryWrapper);
            }

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", id);
            remove(userQueryWrapper);
        }

        return true;
    }

    // 根据id获取用户的信息
    private User getUserInfoUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    // 根据id获取用户的信息
    private User getUserInfoId(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    // 根据username获取用户权限星系
    private UserRole getUserRoleUsername(String username) {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_username", username);

        UserRole userRole;
        try {
            userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return userRole;
    }

    // 获取当前角色的菜单列表
    private List<Menu> getRoleMenus(Integer roleId) {
        QueryWrapper<RoleMenu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("role_id", roleId);
        List<Integer> menuIds = roleMenuMapper.selectList(queryWrapper1).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());

        // 查出系统所有的菜单
        List<Menu> menus = menuService.findMenus();

        // 最后筛选完之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf 移除children 里面不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}
