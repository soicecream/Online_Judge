package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;

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

    User login(User user);

    Boolean register(User user);

    Boolean updatePassword(User user);

    Boolean resetPassword(User user);

    Boolean addOneUser(User user);

    Boolean addListUser(Collection<User> user);

    Boolean MyRemoveById(Integer id);

    Boolean MyRemoveByIds(List<Integer> list);
}
