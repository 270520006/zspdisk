package com.zsp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetNowUtils {
     public static Date getNow() {
         try {
             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
             String format = simpleDateFormat.format(new Date());
             return simpleDateFormat.parse(format);
         } catch (ParseException e) {
             e.printStackTrace();
         }
     return null;
     }
}
