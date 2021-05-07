package com.zsp.controller;

import cn.hutool.core.lang.UUID;
import com.zsp.mapper.UserFileMapper;
import com.zsp.mapper.UserFolderMapper;
import com.zsp.pojo.User;
import com.zsp.pojo.UserFile;
import com.zsp.pojo.UserFolder;
import com.zsp.utils.FileSizeHelper;
import com.zsp.utils.GetNowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShareController {
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    UserFolderMapper userFolderMapper;
    /**
     * 文件分享
     * @param shareFileId 文件id
     * @param session 获取用户信息的session
     * @return
     */
    @GetMapping(value="/user/shareFile/{shareFileId}")
    public  String deleteFile(@PathVariable("shareFileId") int shareFileId, HttpSession session, Model model){
        Integer num=(Integer)session.getAttribute("uploadId");
        User user = (User) session.getAttribute("user");
        if (user==null)
        {
            return "redirect:/";
        }
        session.setAttribute("shareFileId",shareFileId);
        if (num==0)
        { return "redirect:/user/share";}  //改
        return "redirect:/user/share/"+num;  //改
    }

    @GetMapping(value = "/user/share")
    public String shareFile(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
//        查找用户根目录文件夹
        List<UserFolder> userFolders = userFolderMapper.queryByUserId(user.getUserId());
//        查找用户根目录下文件
        List<UserFile> userFiles = userFileMapper.queryByUserId(user.getUserId());
//       判断当前目录下是否有文件，有的话计算大小存入集合
        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
//                查找文件大小并且转换为适用的单位
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
//                查找源文件url
            }
            model.addAttribute("fileSize", fileSize);
        }
        model.addAttribute("userFiles",userFiles);
        model.addAttribute("userFolders",userFolders);
//            告诉其父id为多少
        session.setAttribute("uploadId",0);
        return "user/share";
    }

    @RequestMapping("/user/share/{parentId}")
    public String shareFilePage(@PathVariable("parentId") int parentId, HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
//        告诉下载源父级id
        session.setAttribute("uploadId",parentId);
        List<UserFolder> userFolders = userFolderMapper.queryByParentId(user.getUserId(),parentId);
        List<UserFile> userFiles = userFileMapper.queryByParentId(user.getUserId(), parentId);
        model.addAttribute("userFiles",userFiles);
        model.addAttribute("userFolders",userFolders);
        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }

        return "user/share";

    }

    @GetMapping("/user/share/getShareFile")
    public String getShareFile(HttpSession session){

        User user =(User) session.getAttribute("user");
//        获取当前的分享文件id
        int  shareFileId = (int) session.getAttribute("shareFileId");
        System.out.println(shareFileId);
//        告诉下载源当前id   就是下载文件的当前id
        Integer nowId =(Integer) session.getAttribute("uploadId");
        Integer parentId = userFolderMapper.queryParentId(user.getUserId(), nowId);
        UserFile userShareFile = userFileMapper.queryByFileId(shareFileId);

        Date now = GetNowUtils.getNow();
        UserFile userFile =new UserFile(user.getUserId(),nowId,userShareFile.getOriginId(),userShareFile.getFileName(),userShareFile.getFileSize(),userShareFile.getFileType(),1,now,now,now);
        userFileMapper.addFile(userFile);
        session.removeAttribute("shareFileId");
        if (parentId!=0)
        {return "redirect:/user/home/"+parentId; }
        return "redirect:/user/home";
    }



    @GetMapping("/user/share/backPage")
    public String backPage(HttpSession session){
        User user =(User) session.getAttribute("user");

//        告诉下载源当前id   就是下载文件的当前id
        Integer nowId =(Integer) session.getAttribute("uploadId");

        Integer parentId = userFolderMapper.queryParentId(user.getUserId(), nowId);

        if (parentId!=0)
        {return "redirect:/user/share/"+parentId; }
        return "redirect:/user/share";
    }

}
