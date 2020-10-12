package com.zsp.mapper;

import com.zsp.pojo.UserFolder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserFolderMapper {
    List<UserFolder> queryByUserId(int userId);
    List<UserFolder> queryByParentId(int userId,int parentId);
}
