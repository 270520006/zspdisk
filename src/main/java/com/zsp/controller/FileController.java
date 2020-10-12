package com.zsp.controller;

import com.zsp.mapper.OriginFileMapper;
import com.zsp.mapper.UserFileMapper;
import com.zsp.mapper.UserFolderMapper;
import com.zsp.pojo.OriginFile;
import com.zsp.pojo.User;
import com.zsp.pojo.UserFile;
import com.zsp.pojo.UserFolder;
import com.zsp.utils.FileSizeHelper;
import com.zsp.utils.FileSizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileController {
    @Autowired
    UserFolderMapper userFolderMapper;
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    OriginFileMapper originFileMapper;

    @RequestMapping("/user/home")
    public String home(HttpSession session,Model model){
        User user =(User) session.getAttribute("user");
        List<UserFolder> userFolders = userFolderMapper.queryByUserId(user.getUserId());
        List<UserFile> userFiles = userFileMapper.queryByUserId(user.getUserId());
        Map<Integer,String> fileSize= new HashMap<>();
        for (UserFile userFile : userFiles) {
            fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
        }

        model.addAttribute("fileSize",fileSize);
        model.addAttribute("userFolders",userFolders);
        model.addAttribute("userFiles",userFiles);

        return "user/home";

    }
    @RequestMapping("/user/home/{parentId}")
    public String deepPage(@PathVariable("parentId") int parentId, HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFolder> userFolders = userFolderMapper.queryByParentId(user.getUserId(),parentId);
        List<UserFile> userFiles = userFileMapper.queryByParentId(user.getUserId(), parentId);
        model.addAttribute("userFiles",userFiles);
        model.addAttribute("userFolders",userFolders);
        return "user/home";

    }
    @GetMapping("/user/queryFileName")
    public String queryFileName(  String fileName, HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        

        return "user/home";

    }

}
