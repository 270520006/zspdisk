package com.zsp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int userId;
    private String username;
    private String password;
    private String nickname;
    private String realname;
    private int gender;
    private String phone;
    private String email;
    private String info;
    private int level;
    private int isVip;
    private long memorySize;
    private long usedSize;
    private int privateStatus;
    private String privatePass;
}
