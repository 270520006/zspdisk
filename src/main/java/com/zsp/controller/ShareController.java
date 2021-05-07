package com.zsp.controller;

import cn.hutool.core.lang.UUID;
import com.zsp.pojo.User;
import com.zsp.pojo.UserFile;
import com.zsp.pojo.UserFolder;
import com.zsp.utils.GetNowUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ShareController {
    /**
     * 文件分享
     * @param fileId 文件id
     * @param session 获取用户信息的session
     * @return
     */
    @GetMapping(value="/user/shareFile/{fileId}")
    public  String deleteFile(@PathVariable("fileId") int fileId, HttpSession session){
        Integer num=(Integer)session.getAttribute("uploadId");
        if (session.getAttribute("user")==null)
        {
            return "redirect:/";
        }
        
        if (num==0)
        { return "redirect:/user/home";}
        return "redirect:/user/home/"+num;
    }
}
