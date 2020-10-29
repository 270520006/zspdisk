package com.zsp.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFolder {
    private int folderId;
    private int userId;
    private int parentId;
    private String folderName;
    private int folderStatus;
    private Date createTime;
    private Date modifyTime;
    private Date deleteTime;
}
