package com.zsp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetFileMD5 {

public static String getMD5Three(String path) {
           BigInteger bi = null;
           try {
                   byte[] buffer = new byte[8192];
                   int len = 0;
                   MessageDigest md = MessageDigest.getInstance("MD5");
                   File f = new File(path);
                   FileInputStream fis = new FileInputStream(f);
                   while ((len = fis.read(buffer)) != -1) {
                         md.update(buffer, 0, len);
                    }
                    fis.close();
                    byte[] b = md.digest();
                    bi = new BigInteger(1, b);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return bi.toString(16);
        }
}
