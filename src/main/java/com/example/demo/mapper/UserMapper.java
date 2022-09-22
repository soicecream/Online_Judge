package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    List<User> findAll();

    @Insert("insert into user(username, password, nickname, email, phone) " +
                     "values (#{username}, #{password}, #{nickname}, #{email}, #{phone})")
    int insert(User user);

    int update(User user);

    @Delete("delete from user where id = #{id}")
    Integer deleteById(@Param("id") Integer id);
}
