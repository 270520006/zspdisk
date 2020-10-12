package com.zsp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFile {
    private  int fileId;
    private  int userId;
    private  int parentId;
    private  int originId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private int fileStatus;
    private Date createTime;
    private Date modifyTime;
    private Date deleteTime;
}
