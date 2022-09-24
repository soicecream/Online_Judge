package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServer extends ServiceImpl<UserMapper, User> {

    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

}
