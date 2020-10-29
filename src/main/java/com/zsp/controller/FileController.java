package com.zsp.controller;

import com.zsp.mapper.OriginFileMapper;
import com.zsp.mapper.UserFileMapper;
import com.zsp.mapper.UserFolderMapper;
import com.zsp.pojo.User;
import com.zsp.pojo.UserFile;
import com.zsp.pojo.UserFolder;
import com.zsp.utils.FileSizeHelper;
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


    /**
     * 用户主页
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home")
    public String home(HttpSession session,Model model){
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

        return "user/home";

    }
    @RequestMapping("/user/notice")
    public String notice(){
        return "user/notice";
    }

    /**
     * 多级目录
     * @param parentId   这个是要点击的文件夹的当前id 但是是下一级文件的父亲id
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/{parentId}")
    public String deepPage(@PathVariable("parentId") int parentId, HttpSession session, Model model){
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

        return "user/home";

    }

    /**
     * 多文件下载页面的跳转
     * @return
     */
    @RequestMapping("/user/uploadPage")
    public String uploadPage(){
        return "user/uploadPage";
    }


    /**
     * 查找功能
     * @param fileName
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/user/queryFileName")
    public String queryFileName(String fileName, HttpSession session, Model model){
        User user =(User) session.getAttribute("user");

        if (fileName==null||"".equals(fileName))
        {
            return "redirect:/user/home";
        }
        else
        {

            List<UserFolder> userFolders = userFolderMapper.queryByFolderName(user.getUserId(),fileName);
            List<UserFile> userFiles = userFileMapper.queryByFileName(user.getUserId(), fileName);
            if (userFiles.size()>=1) {
                Map<Integer, String> fileSize = new HashMap<>();
                for (UserFile userFile : userFiles) {
                    fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
                }
                model.addAttribute("fileSize", fileSize);
            }
            model.addAttribute("userFiles",userFiles);
            model.addAttribute("userFolders",userFolders);
            return "user/home";

        }
    }


    /**
     * 对所有分区页面进行管理
     * @return
     */

    @RequestMapping("/user/zones")
    public String zones(){
        return "user/zones";
    }


    /**
     * 图片分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/photos")
    public String queryPhotos(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypePhoto(user.getUserId());
        model.addAttribute("userFiles",userFiles);
         if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }
        return "user/zones";
    }
    /**
     * 文档分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/document")
    public String queryDocument(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeDocument(user.getUserId());
        model.addAttribute("userFiles",userFiles);

        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }
        return "user/zones";
    }
    /**
     * 视频分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/video")
    public String queryVideo(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeVideo(user.getUserId());
        model.addAttribute("userFiles",userFiles);

        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }
        return "user/zones";
    }
    /**
     * 音乐分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/music")
    public String queryMusic(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeMusic(user.getUserId());
        model.addAttribute("userFiles",userFiles);

        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }
        return "user/zones";
    }


    /**
     * 种子分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/bt")
    public String queryBT(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeBT(user.getUserId());
        model.addAttribute("userFiles",userFiles);

        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }
        return "user/zones";
    }

    /**
     * 压缩文件分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/compressed")
    public String queryCompressedFile(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeCompressedFile(user.getUserId());
        model.addAttribute("userFiles",userFiles);

        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }
        return "user/zones";
    }

    /**
     * 回收站的分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/recycle")
    public String queryRecycle(HttpSession session, Model model){
        User user =(User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryRecycleFileByUserId(user.getUserId());
        List<UserFolder> userFolders=userFolderMapper.queryRecycleFolderByUserId(user.getUserId());
        model.addAttribute("userFiles",userFiles);
        model.addAttribute("userFolders",userFolders);
        if (userFiles.size()>=1) {
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile : userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }
        return "user/recycle";
    }


}
