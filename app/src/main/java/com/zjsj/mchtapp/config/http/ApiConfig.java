package com.zjsj.mchtapp.config.http;

import android.content.Context;
import android.text.TextUtils;

import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.DesUtil;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.TelePhoneUtil;

import java.security.PublicKey;

public class ApiConfig {
//    public static String BASE_URL = "http://localhost:8080/mchtAppUserApi/";
     public static final String BASE_URL = "http://192.168.100.66:9080/mchtAppUserApi/";
     private static  String uuid=null;
     public static final String TRANSMIT_KEYTYPE="RSA";
     private static String TRANSMIT_PUBLICKEY=null;
     public static final String STORE_KEYTYPE="RSA";
     private static String STORE_PUBLICKEY=null;
     private static String STORE_PRIVATEKEY=null;
     public static String getAppUUID(Context context)
     {
          if(TextUtils.isEmpty(uuid))
          {
               uuid= TelePhoneUtil.getUtdid(context);
          }
          return uuid;
     }
     public static String getPassWord(String pwdStr, String pwdEncrypt) {
          if (pwdEncrypt.equals("NONE")) {
               return pwdStr;
          }
          else if (pwdEncrypt.equals("RSA")) {
               PublicKey publicKey = RSAUtils.loadPublicKey(TRANSMIT_PUBLICKEY);
               byte[] dataEnt = RSAUtils.encryptData(pwdStr.getBytes(), publicKey);
               return Base64.encode(dataEnt);
          }
          else if (pwdEncrypt.equals("RSAMD5")) {
               PublicKey publicKey = RSAUtils.loadPublicKey(TRANSMIT_PUBLICKEY);
               byte[] dataEnt = RSAUtils.encryptData(EncryptUtils.encodingMD5(pwdStr).getBytes(), publicKey);
               return Base64.encode(dataEnt);
          }
          else if (pwdEncrypt.equals("3DES")) {
               return DesUtil.encryptString(pwdStr, TRANSMIT_PUBLICKEY);
          }
          else if (pwdEncrypt.equals("3DESMD5")) {
               return DesUtil.encryptString(EncryptUtils.encodingMD5(pwdStr), TRANSMIT_PUBLICKEY);
          }
          else if (pwdEncrypt.equals("MD5")) {
               return EncryptUtils.encodingMD5(pwdStr);
          }
          else {
               return null;
          }
     }
}
