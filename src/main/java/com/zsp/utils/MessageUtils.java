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

/**
 * map里的TemplateCode就是你拿到的TemplateCode，你的模板名
 * phone是你要发送给的人的电话号码
 *
 */
public class MessageUtils {
    public static boolean sendMessage(String phone, Map<String,Object> code,String TemplateCode){

//            下面三个参数分别填入regionld、accessKeyld、secret 这个是固定配置不用变动
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "xxx", "xxxx");
            IAcsClient client = new DefaultAcsClient(profile);

//            使用的短信包版本
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

//            TemplateParam

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
