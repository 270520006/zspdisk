package com.zsp.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import com.zsp.mapper.OriginFileMapper;
import com.zsp.mapper.UserFileMapper;
import com.zsp.pojo.OriginFile;

import com.zsp.pojo.User;
import com.zsp.pojo.UserFile;
import com.zsp.utils.GetFileMD5;
import com.zsp.utils.GetNowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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

    /**
     * 多文件上传
     * @param file
     * @param session
     * @return
     */
    @Transactional
    @PostMapping(value="/user/uploadFiles")
    @ResponseBody
    public String uploadSource(@RequestParam("file") MultipartFile file, HttpSession session,HttpServletRequest request    ) {
//        获取父类文件id
        Integer uploadId = (Integer)session.getAttribute("uploadId");
//        获取用户信息
        User user =(User) session.getAttribute("user");

//        String pathString = null;//windows下创建下载路径
        String url= null; //linux 下创建下载路径
        String filename =null;
        if(file!=null) {
            //获取上传的文件名称
            filename = file.getOriginalFilename();
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


//            pathString = "d:/" +filename;//上传到本地 Windows下
              url=request.getServletContext().getRealPath("/") +filename; //linux下创建下载路径




        }

        try {
//            File files=new File(pathString);//在内存中创建File文件映射对象 Windows下

            File files=new File(url); //linux 创建上传路径
            //打印Windows下查看上传路径
//            System.out.println(pathString);
//            System.out.println(url); //打印linux下路径
            if(!files.getParentFile().exists()){//判断映射文件的父文件是否真实存在
                files.getParentFile().mkdirs();//创建所有父文件夹
            }
            file.transferTo(files);//采用file.transferTo 来保存上传的文件

//          ****************上传到数据库里*************************
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


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + "保存到您当前浏览文件夹下" + "\"}"; //Windows下返回
//        return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + url + "\"}"; //linux 下创建
    }


}


