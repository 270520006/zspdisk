package com.zsp.mapper;

import com.zsp.pojo.OriginFile;
import com.zsp.pojo.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;


@Mapper
@Repository
public interface OriginFileMapper {
    OriginFile queryById(int originFileId);
    int addOriginFile(OriginFile originFile);
    OriginFile queryByURL(String fileUrl);
    OriginFile queryByMD5(String fileMd5);
    int updateOriginFile(OriginFile originFile);

}
