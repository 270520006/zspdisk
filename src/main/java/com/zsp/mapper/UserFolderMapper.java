package com.zsp.mapper;

import com.zsp.pojo.UserFile;
import com.zsp.pojo.UserFolder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface UserFolderMapper {
    List<UserFolder> queryByUserId(int userId);
    List<UserFolder> queryByParentId(int userId,int parentId);
    List<UserFolder> queryByFolderName(int userId,String folderName);
    Integer queryParentId(int userId,int folderId);
    Integer updateFolder(UserFolder userFolder);
    Integer addNewFolder(UserFolder userFolder);
    List<UserFolder> queryRecycleFolderByUserId(int userId );

}
