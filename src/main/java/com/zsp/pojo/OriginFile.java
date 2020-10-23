package com.zsp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OriginFile {
    private int originFileId;
    private String fileMd5;
    private Long fileSize;
    private String fileType;
    private String fileUrl;
    private int fileCount;
    private int fileStatus;
    private Date createTime;
    private Date modifyTime;

    public OriginFile(String fileMd5, Long fileSize, String fileType, String fileUrl, int fileCount, int fileStatus, Date createTime, Date modifyTime) {
        this.fileMd5 = fileMd5;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.fileCount = fileCount;
        this.fileStatus = fileStatus;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
}
