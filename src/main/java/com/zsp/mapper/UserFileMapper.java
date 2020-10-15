package com.zsp.mapper;

import com.zsp.pojo.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface UserFileMapper {
    List<UserFile> queryByUserId(int userId);
    List<UserFile> queryByParentId(int userId ,int parentId);
    List<UserFile> queryByFileName(int userId ,String fileName);

}
