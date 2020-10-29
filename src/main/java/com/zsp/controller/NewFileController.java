package com.zsp.controller;

import com.zsp.mapper.UserFolderMapper;
import com.zsp.pojo.User;
import com.zsp.pojo.UserFolder;
import com.zsp.utils.GetNowUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class NewFileController {
    @Autowired
    UserFolderMapper userFolderMapper;
    @PostMapping("/user/newFolder")
    public Integer  newFolder(HttpSession session, String fileName)  {
        if ("".equals(fileName))
        {
            fileName="新建文件夹";
        }
        try {
            UserFolder userFolder =new UserFolder();
//        获取时间

            Date parse = GetNowUtils.getNow();
//        获取用户id
            User user = (User) session.getAttribute("user");
            int userId = user.getUserId();
//        获取父亲文件id
            Integer parentId = (Integer) session.getAttribute("uploadId");

            userFolder.setCreateTime(parse);
            userFolder.setModifyTime(parse);
            userFolder.setFolderName(fileName);
            userFolder.setUserId(userId);
            userFolder.setParentId(parentId);
            Integer integer = userFolderMapper.addNewFolder(userFolder);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 400;
    }
}
