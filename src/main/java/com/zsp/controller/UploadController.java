package com.zsp.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
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
        //                获取当前时间
        Date now =GetNowUtils.getNow();
        if (file != null && !file.isEmpty()){
            try {
//                获取文件名字
                String filename=file.getOriginalFilename();
//                文件下载地址

//      window/linux文件下载1.0    到tomcat的工作目录下 ，但问题太多了，因为每创建一个tomcat就要重新更换磁盘文件位置
                String url=request.getServletContext().getRealPath("/") +filename;
                System.out.println(url);
//      window文件下载地址2.0 window下截取字符
//      但这里需要先去tomcat的temp下创建一个files文件夹
//      这里使用uuid是为了防止上传文件跟我们的源文件名字重名

//                String url=StrUtil.sub(request.getServletContext().getRealPath("/"),0,41)+"\\files\\"+IdUtil.randomUUID()+"="+filename  ;
//
//                System.out.println(url);

//        linux文件下载地址2.0版本   直接存到tmp下files文件夹下，（我们在/tmp/下创建了一个files用来存储）
//                String url= "/tmp/files/"+filename;
//        linux3.0版本
//                String url= "/tmp/files/"+IdUtil.randomUUID()+"="+filename  ;

                file.transferTo(new File(url) );
                String md5=GetFileMD5.getMD5Three(url);
                OriginFile queryByMD5 = originFileMapper.queryByMD5(md5);

//                1如果发现md5值相同则进行源文件表的数量+1
//                2把存有的源    文件添加进用户文件表里
//                3删除上传过来的源文件并且反馈给用户上传成功
                if ( queryByMD5!=null)
                {

//                    源文件数量+1
                    queryByMD5.setFileCount(queryByMD5.getFileCount()+1);

                    originFileMapper.updateOriginFile(queryByMD5);
//                    把存有的源文件添加进用户文件表里
                    UserFile userFile =new UserFile(user.getUserId(),uploadId,queryByMD5.getOriginFileId(),filename,queryByMD5.getFileSize(),queryByMD5.getFileType(),1,now,now,now);
                    userFileMapper.addFile(userFile);
//                    删除上传过来的源文件并且反馈给用户上传成功
                    FileUtil.del(url);

                    result.put("code", 200);
                    result.put("msg", "success");
                    return result;
                }else
                {
                    //                获取文件大小
                    Long fileSize=file.getSize();
//                获取文件类型
                    String type = FileTypeUtil.getType(FileUtil.file(url));
//                获取当前时间

//                System.out.println(file.getSize());   //获取文件大小
//                把上传的文件的源文件写入数据库中
                    OriginFile originFile =new OriginFile( GetFileMD5.getMD5Three(url),fileSize,type,url,1,1,now,now);
                    originFileMapper.addOriginFile(originFile);

//                把上传的文件的文件列表写入数据库中
                    UserFile userFile =new UserFile(user.getUserId(),uploadId,originFileMapper.queryByURL(url).getOriginFileId(),filename,fileSize,type,1,now,now,now);
                    userFileMapper.addFile(userFile);

                }


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
//        获取当前时间
        Date now =GetNowUtils.getNow();

        String url= null; // 创建下载路径
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
            //      window/linux文件下载1.0    到tomcat的工作目录下 ，但问题太多了，因为每创建一个tomcat就要重新更换磁盘文件位置
//            url=request.getServletContext().getRealPath("/") +filename;

//      window文件下载地址2.0 window下截取字符,但这里需要先去tomcat的temp下创建一个files文件夹
            //      这里使用uuid是为了防止上传文件跟我们的源文件名字重名
//            url=StrUtil.sub(request.getServletContext().getRealPath("/"),0,41)+"\\files\\"+IdUtil.randomUUID()+"="+filename  ;
//                System.out.println(url);

//            linux文件下载地址2.0 window下截取字符,但这里需要先去tomcat的temp下创建一个files文件夹
//           url= "/tmp/files/"+filename;

//            linux3.0版本
            url= "/tmp/files/"+IdUtil.randomUUID()+"="+filename  ;

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
//                1如果发现md5值相同则进行源文件表的数量+1
//                2把存有的源文件添加进用户文件表里
//                3删除上传过来的源文件并且反馈给用户上传成功
            String md5=GetFileMD5.getMD5Three(url);
            OriginFile queryByMD5 = originFileMapper.queryByMD5(md5);
            if ( queryByMD5!=null)
            {

//                    源文件数量+1
                queryByMD5.setFileCount(queryByMD5.getFileCount()+1);

                originFileMapper.updateOriginFile(queryByMD5);
//                    把存有的源文件添加进用户文件表里
                UserFile userFile =new UserFile(user.getUserId(),uploadId,queryByMD5.getOriginFileId(),filename,queryByMD5.getFileSize(),queryByMD5.getFileType(),1,now,now,now);
                userFileMapper.addFile(userFile);
//                    删除上传过来的源文件并且反馈给用户上传成功
                FileUtil.del(url);
                return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + "保存到您当前浏览文件夹下" + "\"}"; //Windows下返回

            }else
            {
                //                获取文件大小
                Long fileSize=file.getSize();
//                获取文件类型
                String type = FileTypeUtil.getType(FileUtil.file(url));
//                获取当前时间

//                System.out.println(file.getSize());   //获取文件大小
//                把上传的文件的源文件写入数据库中
                OriginFile originFile =new OriginFile( GetFileMD5.getMD5Three(url),fileSize,type,url,1,1,now,now);
                originFileMapper.addOriginFile(originFile);

//                把上传的文件的文件列表写入数据库中
                UserFile userFile =new UserFile(user.getUserId(),uploadId,originFileMapper.queryByURL(url).getOriginFileId(),filename,fileSize,type,1,now,now,now);
                userFileMapper.addFile(userFile);
                return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + "保存到您当前浏览文件夹下" + "\"}"; //Windows下返回
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + "保存到您当前浏览文件夹下" + "\"}"; //Windows下返回

    }


}


