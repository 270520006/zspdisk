package com.zsp.mapper;

import com.zsp.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User queryById(Integer userId);
    User queryByUsername(String username);

    int updateUser(User user);
    int addUser(String username,String password,String phone);
}
