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
    List<UserFile> queryByFileTypePhoto(int userId);
    List<UserFile> queryByFileTypeDocument(int userId);
    List<UserFile> queryByFileTypeVideo(int userId);
    List<UserFile> queryByFileTypeMusic(int userId);
    List<UserFile> queryByFileTypeBT(int userId);
    List<UserFile> queryByFileTypeCompressedFile(int userId);

}
