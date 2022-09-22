package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserServer userServer;

    @PostMapping
    public Integer save(@RequestBody User user) {
        return userServer.save(user);
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return userMapper.deleteById(id);
    }

    @GetMapping
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @GetMapping("/page")
    public List<User> findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
        return null;
    }


}
