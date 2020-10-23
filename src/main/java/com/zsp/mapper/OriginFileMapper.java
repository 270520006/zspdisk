package com.zsp.mapper;

import com.zsp.pojo.OriginFile;
import com.zsp.pojo.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;



@Mapper
@Repository
public interface OriginFileMapper {
    OriginFile queryById(int originFileId);
    int addOriginFile(OriginFile originFile);
    OriginFile queryByURL(String fileUrl);
}
