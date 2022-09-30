package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Constants;
import com.example.demo.controller.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.TokenUtils;
import org.springframework.stereotype.Service;

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

    //    登录用户
    @Override
    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one != null) {
            BeanUtil.copyProperties(one, userDto, true); // 从数据库查询用户信息(one 复制到 userDto中)

//            设置token
            String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

//    注册用户
    @Override
    public User register(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one != null) {
            one = new User();
            BeanUtil.copyProperties(userDto, one, true);
            save(one); // 把copy完之后的用户对象存储到数据库中
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

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

}
