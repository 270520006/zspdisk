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

    public UserFile(int userId, int parentId, int originId, String fileName, Long fileSize, String fileType, int fileStatus, Date createTime, Date modifyTime, Date deleteTime) {
        this.userId = userId;
        this.parentId = parentId;
        this.originId = originId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.deleteTime = deleteTime;
    }
}
