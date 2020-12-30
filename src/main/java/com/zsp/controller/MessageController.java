package com.zsp.controller;



import cn.hutool.core.util.RandomUtil;



import com.zsp.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.util.HashMap;


@Slf4j
@RestController
public class MessageController {
    @PostMapping("/message")
    @ResponseBody
    public String  getMessage( HttpSession session,String phone ){

        String codeNum =""+ RandomUtil.randomInt(1000, 9999);
        session.setAttribute("codeNum",codeNum);
        HashMap<String,Object> code =new HashMap<>();
        code.put("code",codeNum);


        try {

            MessageUtils.sendMessage(phone,code,"SMS_205455159");
        } catch (Exception e) {
            e.printStackTrace();
            return "404";
        }


        return "200";

    }


}
