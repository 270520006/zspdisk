package com.zsp.mapper;

import com.zsp.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<User> queryAll();
    User queryById(Integer userId);
    User queryByUsername(String username);

    int updateUser(User user);
    int addUser(String username,String password,String phone);
}
