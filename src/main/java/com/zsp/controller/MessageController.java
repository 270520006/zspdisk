package com.zsp.controller;



import cn.hutool.core.util.RandomUtil;
import com.zsp.utils.MessageUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;


@RestController
public class MessageController {
    @PostMapping("/message")
    public void  getMessage(String phone, HttpSession session){
        String codeNum =""+ RandomUtil.randomInt(1000, 9999);
        session.setAttribute("codeNum",codeNum);

        HashMap<String,Object> code =new HashMap<>();
        code.put("code",codeNum);
        MessageUtils.sendMessage(phone,code,"SMS_205455159");
    }
}
