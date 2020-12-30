package com.zsp.mapper;

import com.zsp.pojo.NoticeUpdate;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


import java.util.List;

@Mapper
@Repository
public interface NoticeUpdateMapper {
    List<NoticeUpdate> queryAll();
    int addNotice(NoticeUpdate notice);
}
