package com.zsp.mapper;

import com.zsp.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User queryById(Integer userId);
    User queryByUsername(String name);
    int updateUser(User user);
}
