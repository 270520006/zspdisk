package com.zsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoadController {
    @PostMapping("user/upload")
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file, HttpSession session){
        Integer uploadId = (Integer)session.getAttribute("uploadId");
        System.out.println(uploadId);
        Map<String, Object> result = new HashMap<>();
        if (file != null && !file.isEmpty()){
            try {
                file.transferTo(new File("d:/"+file.getOriginalFilename()));
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
}


