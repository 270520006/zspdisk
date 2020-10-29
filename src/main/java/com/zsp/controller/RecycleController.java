package com.zsp.controller;

import com.zsp.mapper.OriginFileMapper;
import com.zsp.mapper.UserFileMapper;
import com.zsp.mapper.UserFolderMapper;
import com.zsp.pojo.OriginFile;
import com.zsp.pojo.UserFile;
import com.zsp.pojo.UserFolder;
import com.zsp.utils.GetNowUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Controller
public class RecycleController {
    @Autowired
    UserFolderMapper userFolderMapper;
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    OriginFileMapper originFileMapper;


    /**
     *   把文件删除并加入回收站，这里使用的是修改状态码为2
     * @param fileId
     * @param session
     * @return
     */
    @Transactional
    @GetMapping(value="/user/deleteFile/{fileId}")
    public  String deleteFile(@PathVariable("fileId") int fileId, HttpSession session){
        Integer num=(Integer)session.getAttribute("uploadId");
        Date now= GetNowUtils.getNow();
        UserFile userFile = new UserFile();
        userFile.setFileId(fileId);
        userFile.setDeleteTime(now);
        userFile.setModifyTime(now);
        userFile.setFileStatus(2);
        userFileMapper.updateFile(userFile);
        if (num==0)
        { return "redirect:/user/home";}
        return "redirect:/user/home/"+num;
    }

    /**
     * 把文件夹放进回收站
     * @param folderId
     * @param session
     * @return
     */
    @Transactional
    @GetMapping(value="/user/deleteFolder/{folderId}")
    public  String deleteFolder(@PathVariable("folderId") int folderId, HttpSession session){
        Integer num=(Integer)session.getAttribute("uploadId");
        Date now= GetNowUtils.getNow();
        UserFolder userFolder= new UserFolder();
        userFolder.setModifyTime(now);
        userFolder.setDeleteTime(now);
        userFolder.setFolderId(folderId);
        userFolder.setFolderStatus(2);

        userFolderMapper.updateFolder(userFolder);
        if (num==0)
        { return "redirect:/user/home";}
        return "redirect:/user/home/"+num;
    }
    /**
     * 恢复文件按钮
     * @param fileId
     * @param session
     * @return
     */
    @Transactional
    @GetMapping(value="/user/recoverFile/{fileId}")
    public  String recoverFile(@PathVariable("fileId") int fileId, HttpSession session){
        Integer num=(Integer)session.getAttribute("uploadId");
        Date now= GetNowUtils.getNow();
        UserFile userFile = new UserFile();
        userFile.setFileId(fileId);
        userFile.setModifyTime(now);
        userFile.setFileStatus(1);
        userFileMapper.updateFile(userFile);

        return "redirect:/user/home/recycle";
    }
    /**
     * 恢复文件夹按钮
     * @param folderId
     * @param session
     * @return
     */
    @Transactional
    @GetMapping(value="/user/recoverFolder/{folderId}")
    public  String recoverFolder(@PathVariable("folderId") int folderId, HttpSession session){
        Integer num=(Integer)session.getAttribute("uploadId");
        Date now= GetNowUtils.getNow();


        UserFolder userFolder= new UserFolder();

        userFolder.setModifyTime(now);
        userFolder.setFolderStatus(1);
        userFolder.setFolderId(folderId);

        userFolderMapper.updateFolder(userFolder);

        return "redirect:/user/home/recycle";
    }

    /**
     * 彻底删除文件方法，此方法会把状态码改为3
     * @param fileId
     * @param session
     * @return
     */
    @Transactional
    @GetMapping(value="/user/removeFile/{fileId}")
    public  String remove(@PathVariable("fileId") int fileId, HttpSession session){
        Integer num=(Integer)session.getAttribute("uploadId");
        Date now= GetNowUtils.getNow();
//        修改状态码
        UserFile userFile = new UserFile();
        userFile.setFileId(fileId);
        userFile.setModifyTime(now);
        userFile.setFileStatus(3);
        userFileMapper.updateFile(userFile);

//        获取源文件id

        Integer originId = userFileMapper.queryOriginIdByFileId(fileId);

//        修改源文件的拥有者数量
        OriginFile originFile =new OriginFile();
        originFile.setOriginFileId(originId);
        int fileCount = originFileMapper.queryById(originId).getFileCount();
        if (fileCount==1)
        {return "redirect:/user/home/recycle";}
        originFile.setFileCount(fileCount -1);
        originFileMapper.updateOriginFile(originFile);
        return "redirect:/user/home/recycle";
    }

    /**
     * 把文件夹彻底删除（文件夹里面的文件暂时未递归删除）
     * @param folderId
     * @param session
     * @return
     */
    @Transactional
    @GetMapping(value="/user/removeFolder/{folderId}")
    public  String removeFolder(@PathVariable("folderId") int folderId, HttpSession session){
        Integer num=(Integer)session.getAttribute("uploadId");
        Date now= GetNowUtils.getNow();


        UserFolder userFolder= new UserFolder();

        userFolder.setModifyTime(now);
        userFolder.setFolderStatus(3);
        userFolder.setFolderId(folderId);

        userFolderMapper.updateFolder(userFolder);

        return "redirect:/user/home/recycle";
    }
}
