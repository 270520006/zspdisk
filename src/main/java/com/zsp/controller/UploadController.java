package com.zsp.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.zsp.mapper.OriginFileMapper;
import com.zsp.mapper.UserFileMapper;
import com.zsp.pojo.OriginFile;

import com.zsp.pojo.User;
import com.zsp.pojo.UserFile;
import com.zsp.utils.GetFileMD5;
import com.zsp.utils.GetNowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    OriginFileMapper originFileMapper;

    @Transactional
    @PostMapping("user/upload")
    @ResponseBody
    public Map<String, Object> upload(HttpServletRequest request,MultipartFile file, HttpSession session){
        Integer uploadId = (Integer)session.getAttribute("uploadId");
        User user =(User) session.getAttribute("user");

//        System.out.println(uploadId);   //获取父亲文件id
        Map<String, Object> result = new HashMap<>();
        if (file != null && !file.isEmpty()){
            try {
//                获取文件名字
                String filename=file.getOriginalFilename();
//                文件下载地址
                String url=request.getServletContext().getRealPath("/") +filename;
                file.transferTo(new File(url) );
//                获取文件大小
                Long fileSize=file.getSize();
//                获取文件类型
                String type = FileTypeUtil.getType(FileUtil.file(url));
//                获取当前时间
                Date now =GetNowUtils.getNow();
//                System.out.println(file.getSize());   //获取文件大小
//                把上传的文件的源文件写入数据库中
                OriginFile originFile =new OriginFile( GetFileMD5.getMD5Three(url),fileSize,type,url,1,1,now,now);
                originFileMapper.addOriginFile(originFile);

//                把上传的文件的文件列表写入数据库中
                UserFile userFile =new UserFile(user.getUserId(),uploadId,originFileMapper.queryByURL(url).getOriginFileId(),filename,fileSize,type,1,now,now,now);
                userFileMapper.addFile(userFile);



                result.put("code", 200);
                result.put("msg", "success");
            } catch (IOException e) {
                result.put("code", -1);
                result.put("msg", "文件上传出错，请重新上传！");
                e.printStackTrace();
            }
        } else {
            result.put("code", -1);
            result.put("msg", "未获取到有效的文件信息，请重新上传!");
        }
        return result;
    }

    @Transactional
    @PostMapping(value="/user/uploadFiles")
    @ResponseBody
    public String uploadSource(@RequestParam("file") MultipartFile file) {
        System.out.println(file);
        String pathString = null;
        if(file!=null) {
            //获取上传的文件名称
            String filename = file.getOriginalFilename();
            //文件上传时，chrome与IE/Edge对于originalFilename处理方式不同
            //chrome会获取到该文件的直接文件名称，IE/Edge会获取到文件上传时完整路径/文件名
            //Check for Unix-style path
            int unixSep = filename.lastIndexOf('/');
            //Check for Windows-style path
            int winSep = filename.lastIndexOf('\\');
            //cut off at latest possible point
            int pos = (winSep > unixSep ? winSep:unixSep);
            if (pos != -1)
                filename = filename.substring(pos + 1);
            pathString = "d:/" +filename;//上传到本地
        }
        try {
            File files=new File(pathString);//在内存中创建File文件映射对象
            //打印查看上传路径
            System.out.println(pathString);
            if(!files.getParentFile().exists()){//判断映射文件的父文件是否真实存在
                files.getParentFile().mkdirs();//创建所有父文件夹
            }
            file.transferTo(files);//采用file.transferTo 来保存上传的文件
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + pathString + "\"}";
    }

    public int addFile(UserFile userFile){
        return userFileMapper.addFile(userFile);
    }
    public int addOriginFile(OriginFile originFile){
        return originFileMapper.addOriginFile(originFile);
    }
}


