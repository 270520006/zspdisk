package com.zsp.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;



import java.util.Map;

public class MessageUtils {
    public static boolean sendMessage(String phone, Map<String,Object> code,String TemplateCode){


            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "xxxxxx", "xxxxx");
            IAcsClient client = new DefaultAcsClient(profile);

//            上面为验证码平台固定配置，不要动
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");

//            自定义参数
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", "zsp网盘");
            request.putQueryParameter("TemplateCode", TemplateCode);



            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(code));
            try {
                CommonResponse response = client.getCommonResponse(request);

                return response.getHttpResponse().isSuccess();
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();

        }
    return false;
    }
}
