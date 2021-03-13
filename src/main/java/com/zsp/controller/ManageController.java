package com.zsp.controller;

import com.mysql.cj.protocol.x.Notice;
import com.zsp.mapper.NoticeUpdateMapper;
import com.zsp.mapper.UserMapper;
import com.zsp.pojo.NoticeUpdate;
import com.zsp.pojo.User;
import com.zsp.utils.GetNowUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ManageController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    NoticeUpdateMapper noticeUpdateMapper;
    @GetMapping("/manage")
    public String loginManage(){
        return "manage";
    }
    @PostMapping("/manageLogin")
    public String manageLogin(String username, String password, Model model, HttpSession session){
//        正常用户登录流程
        if (username.equals("zspzspmanage")&&password.equals("zspzsp123")) {
            Subject subject= SecurityUtils.getSubject();
            UsernamePasswordToken token=new UsernamePasswordToken(username,password);
            try{
                subject.login(token);
                session.setAttribute("user",userMapper.queryByUsername(username));
                return "redirect:/user/manage";
            }catch (UnknownAccountException e){
                model.addAttribute("msg","不存在该用户");
                return "manage";
            }
            catch (IncorrectCredentialsException e){
                model.addAttribute("msg","密码错误");
                return "manage";
            }
        }else
        {
            model.addAttribute("msg", "用户名或密码错误");
            return "manage";
        }


    }
    @GetMapping("/user/manage")
    public String manage(HttpSession session,Model model){
        List<User> userList = userMapper.queryAll();
        model.addAttribute("userList",userList);
        return "user/manage";
    }
    @RequestMapping("/user/manageInformation")
    public String manageInformation(){
        return "user/manageInformation";
    }

    @PostMapping("/user/toSubmitManage")
    public String toSubmit(User user,HttpSession session){
        User user1=(User)session.getAttribute("user");
        user.setUserId( user1.getUserId());
        userMapper.updateUser(user);
        session.setAttribute("user",userMapper.queryById(user.getUserId()));

        return "redirect:/user/manage";
    }
    @RequestMapping("/user/manageSafeInformation")
    public String manageSafeInformation(){
        return "user/manageSafeInformation";
    }

    @PostMapping("/user/toSubmitPrivateManage")
    public String toSubmitPrivate(String privatePass1,String privatePass2,String privateStatus,HttpSession session,Model model){
        System.out.println(privatePass1+"   "+privatePass2+"   "+privateStatus);
        User user=(User)session.getAttribute("user");
        if (!privatePass1.equals(privatePass2) )
        {
            model.addAttribute("error","两次密码输入不同");
            return "user/manageSafeInformation";
        }
        if ("off".equals(privateStatus))
        {
            user.setPrivatePass(null);
            user.setPrivateStatus(0);
            userMapper.updateUser(user);
            return "redirect:/user/manage";
        }
        else {
            user.setPrivateStatus(1);
            user.setPrivatePass(privatePass1);
            userMapper.updateUser(user);
            return "redirect:/user/manage";
        }
    }
    @RequestMapping("/user/manageChangePassword")
    public String changePassword(){
        return "user/manageChangePassword";
    }
    @PostMapping("/user/submitPasswordManage")
    public String submitPassword(String password1,String password2,HttpSession session,Model model ){
        if (password1.equals(password2))
        {
            User user =new User();
            user.setPassword(password1);
            User user1=(User)session.getAttribute("user");
            user.setUserId( user1.getUserId());
            userMapper.updateUser(user);
            session.setAttribute("user",userMapper.queryById(user.getUserId()));
            return "redirect:/user/manage";
        }
        else{
            model.addAttribute("error","两次输入的密码不一致");
            return "redirect:/user/manage";
        }

    }
    @RequestMapping("/user/updateNotice")
    public String updateNotice(){
        return "user/updateNotice";
    }
    @RequestMapping("/user/submitNoticeUpdate")
    public String submitNoticeUpdate(String noticeContent){
        NoticeUpdate noticeUpdate = new NoticeUpdate();
        noticeUpdate.setNoticeDate(GetNowUtils.getNow());
        noticeUpdate.setNoticeContent(noticeContent);
        noticeUpdateMapper.addNotice(noticeUpdate);
        return "redirect:/user/manageNotice";
    }
    @RequestMapping("/user/manageNotice")
    public String manageNotice(Model model){
        List<NoticeUpdate> noticeUpdates = noticeUpdateMapper.queryAll();
        model.addAttribute("noticeUpdates",noticeUpdates);
        return "user/manageNotice";
    }

    @RequestMapping("/user/banUser/{isBan}/{userId}")
    public String banUser(@PathVariable("isBan") Integer isBan,@PathVariable("userId") Integer userId, Model model){
        User user =new User();
        user.setUserId(userId);
        user.setLevel(isBan);
        userMapper.updateUser(user);
        return "redirect:/user/manage";
    }

    @RequestMapping("/user/manageUpdateUser/{userId}")
    public String manageUpdateUser(@PathVariable("userId") Integer userId ,Model model){
        User manageUpdateUser = userMapper.queryById(userId);
        model.addAttribute("manageUpdateUser",manageUpdateUser);
        return "user/manageUpdateUser";
    }
    @RequestMapping("/user/submitManageUpdateUser/{userId}")
    public String submitManageUpdateUser(@PathVariable("userId") Integer userId,User user){
        user.setUserId(userId);
        if (user.getMemorySize()==0)
        {
            user.setMemorySize(1073741824);
        }
        userMapper.updateUser(user);
        return "redirect:/user/manage";
    }

}
