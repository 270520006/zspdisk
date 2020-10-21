package com.zsp.controller;

import com.zsp.mapper.UserFolderMapper;
import com.zsp.pojo.User;
import com.zsp.pojo.UserFolder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class NewFileController {
    @Autowired
    UserFolderMapper userFolderMapper;
    @PostMapping("/user/newFolder")
    public Integer  newFolder(HttpSession session, String fileName)  {
        try {
            UserFolder userFolder =new UserFolder();
//        获取时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String format = simpleDateFormat.format(new Date());
            Date parse = simpleDateFormat.parse(format);
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 400;
    }
}
