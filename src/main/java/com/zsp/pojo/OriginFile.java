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
}
