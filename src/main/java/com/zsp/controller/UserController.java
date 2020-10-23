package com.zsp.controller;

import com.zsp.mapper.UserFolderMapper;
import com.zsp.mapper.UserMapper;
import com.zsp.pojo.User;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserFolderMapper userFolderMapper;
    /**
     * 登陆页面，通过session判断是否登陆过，登陆过直接给予
     * @param session
     * @return
     */

    @RequestMapping({"/","/index.html","/toLogin"})
    public String toLogin(HttpSession session){
        if (session.getAttribute("user")!=null)
        {return   "redirect:/user/home";}
        return "index";

    }
    /**
     *
     * @param username 用户名
     * @param password 密码
     * @param model     传递错误信息的参数
     * @param session   登陆成功后在session里保存，以后登录首页直接跳转
     * @return
     */
    @PostMapping("/login")
    public String login( String username, String password, Model model,HttpSession session){

        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try{
            subject.login(token);
            session.setAttribute("user",userMapper.queryByUsername(username));
            return "redirect:/user/home";
        }catch (UnknownAccountException e){
           model.addAttribute("msg","不存在该用户");
           return "index";
        }
        catch (IncorrectCredentialsException e){
           model.addAttribute("msg","密码错误");
            return "index";
        }

    }

    /**
     * 去注册功能
     * @return
     */
    @RequestMapping("/toRegist")
    public String toRegist(){
        return "regist";
    }
    /**
     * 注册功能
     * @return
     */
    @RequestMapping("/regist")
    public String regist(String username, String password,String phone,Model model,HttpSession session){
        if (password==null || "".equals(password))
        {
             model.addAttribute("msg","密码不能为空");
            return "regist";
        }
        if (phone==null||"".equals(phone))
        {
             model.addAttribute("msg","手机号不能为空");
            return "regist";
        }
        if (username!=null&&!"".equals(username))
        {

            if (userMapper.queryByUsername(username)==null)
            {
                userMapper.addUser(username,password,phone);
                Subject subject= SecurityUtils.getSubject();
                UsernamePasswordToken token=new UsernamePasswordToken(username,password);
                subject.login(token);
                User user=userMapper.queryByUsername(username);
                System.out.println(user);
                session.setAttribute("user",user);

                return "redirect:/user/home";
            }
            else
            {
                model.addAttribute("msg","用户名已存在");
                return "regist";
            }
        }
        else
        {
            model.addAttribute("msg","用户名不能为空");
            return "regist";
        }

    }

    /**
     * 忘记密码页面
     * @return
     */
    @RequestMapping("/forget")
    public String toForget(){
        return "forget";
    }

    /**
     * 忘记密码功能页面
      * @param username
     * @param phone
     * @return
     */
    @PostMapping("/submitForget")
    public String forget(String username,String phone,String code,Model model,HttpSession session){
        User user = userMapper.queryByUsername(username);
        System.out.println(user);
        if (user==null)
        {
            model.addAttribute("msg","该用户不存在");
            return "forget";
        }
        if (phone.equals(user.getPhone())){
            if (code!=null&&!"".equals(code)){

                Subject subject= SecurityUtils.getSubject();
                UsernamePasswordToken token=new UsernamePasswordToken(username,user.getPassword());
                subject.login(token);
                session.setAttribute("user",userMapper.queryByUsername(username));
                return "user/changePassword";
            }
            else
            {
                model.addAttribute("msg","验证码错误");
                return "forget";
            }
        }
        else
        {
            model.addAttribute("msg","用户名对应的手机号错误");
            return "forget";
        }


    }

    /**
     * 修改密码页面
     * @return
     */
    @RequestMapping("/user/changePassword")
    public String changePassword(){
        return "user/changePassword";
    }
    @PostMapping("/user/submitPassword")
    public String submitPassword(String password1,String password2,HttpSession session,Model model ){
        if (password1.equals(password2))
        {
            User user =new User();
            user.setPassword(password1);
            User user1=(User)session.getAttribute("user");
            user.setUserId( user1.getUserId());
            userMapper.updateUser(user);
            session.setAttribute("user",userMapper.queryById(user.getUserId()));
            return "redirect:/user/home";
        }
        else{
            model.addAttribute("error","两次输入的密码不一致");
            return "user/changePassword";
        }

    }


    /**
     * 填写用户信息
     * @return
     */
    @RequestMapping("/user/information")
    public String userInfo(){
        return "user/information";
    }

    /**
     * 填写用户安全信息
     * @return
     */
    @RequestMapping("/user/safeInformation")
    public String safeInformation(){
        return "user/safeInformation";
    }
    /**
     * 提交用户信息并返回首页
     * @return
     */
    @PostMapping("/user/toSubmit")
    public String toSubmit(User user,HttpSession session){
         User user1=(User)session.getAttribute("user");
         user.setUserId( user1.getUserId());
         userMapper.updateUser(user);
         session.setAttribute("user",userMapper.queryById(user.getUserId()));
         return "redirect:/user/home";
    }
    @PostMapping("/user/toSubmitPrivate")
    public String toSubmitPrivate(String privatePass1,String privatePass2,String privateStatus,HttpSession session,Model model){
        System.out.println(privatePass1+"   "+privatePass2+"   "+privateStatus);
        User user=(User)session.getAttribute("user");
        if (!privatePass1.equals(privatePass2) )
        {
        model.addAttribute("error","两次密码输入不同");
        return "user/safeInformation";
        }
        if ("off".equals(privateStatus))
        {
            user.setPrivatePass(null);
            user.setPrivateStatus(0);
            userMapper.updateUser(user);
            return "redirect:/user/home";
        }
        else {
            user.setPrivateStatus(1);
            user.setPrivatePass(privatePass1);
            userMapper.updateUser(user);
            return "user/home";
        }
    }

    @GetMapping("/user/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:index.html";
    }



}
