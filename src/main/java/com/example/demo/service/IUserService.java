package com.example.demo.service;

import com.example.demo.controller.dto.UserDto;
import com.example.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * @since 2022-09-25
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userDto);

    Boolean register(UserDto userDto);

    Boolean updatePassword(UserDto userDto);

    Boolean resetPassword(UserDto userDto);

    Boolean addOneUser(UserDto userDto);

    Boolean addListUser(Collection<UserDto> userDtoList);

    Boolean MyRemoveById(Integer id);

    Boolean MyRemoveByIds(List<Integer> list);
}
