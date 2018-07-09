package com.zjsj.mchtapp.config.http;

import android.content.Context;
import android.text.TextUtils;

import com.ruomm.base.ioc.iocutil.BaseServiceUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.DesUtil;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.zjsj.mchtapp.module.keypair.KeyPairService;

import java.security.KeyPair;
import java.security.PublicKey;

public class ApiConfig {
//    public static String BASE_URL = "http://localhost:8080/mchtAppUserApi/";

     public static String BASE_URL = "http://192.168.3.10:8080/mchtAppUserApi/";
//     public static final String BASE_URL = "http://192.168.100.66:9080/mchtAppUserApi/";
     private static  String uuid=null;
     public static final String TRANSMIT_KEYTYPE="RSA";
//     public static String TRANSMIT_PUBLICKEY=null;
     public static String TRANSMIT_DESKEY=null;
     public static PublicKey TRANSMIT_RSAKEY=null;
     public static final String STORE_KEYTYPE="RSA";
     public static String STORE_PUBLICKEY=null;
     public static String STORE_PRIVATEKEY=null;
     public static String getAppUUID(Context context)
     {
          if(TextUtils.isEmpty(uuid))
          {
               uuid= TelePhoneUtil.getUtdid(context);
          }
          return uuid;
     }
     public static boolean isKeyPairOk()
     {
          if(StringUtils.isEmpty(STORE_PUBLICKEY)||StringUtils.isEmpty(STORE_PRIVATEKEY))
          {
               return false;
          }
          if("RSA".equals(TRANSMIT_KEYTYPE))
          {
               if(null==TRANSMIT_RSAKEY)
               {
                    return false;
               }
               else return true;
          }
          else if("3DES".equals(TRANSMIT_KEYTYPE))
          {
               if(StringUtils.isEmpty(TRANSMIT_DESKEY))
               {
                    return false;
               }
               else{
                    return true;
               }
          }
          else{
               return false;
          }
     }
     public static  boolean isKeyPairOk(Context context){
          boolean flag=isKeyPairOk();
          if(flag||null==context)
          {
               return flag;
          }
          try {
               BaseServiceUtil.startService(context, KeyPairService.class,1);
          }catch (Exception e)
          {
               e.printStackTrace();
          }

          return flag;
     }
     public static void loadTransmitKey(String keyStr)
     {
          if("RSA".equals(TRANSMIT_KEYTYPE))
          {
               TRANSMIT_RSAKEY=RSAUtils.loadPublicKey(keyStr);
          }
          else if("3DES".equals(TRANSMIT_KEYTYPE))
          {
              TRANSMIT_DESKEY=new String(keyStr);
          }
          else{
               return;
          }
     }
     public static String getPassWord(String pwdStr, String pwdEncrypt) {
          if (pwdEncrypt.equals("NONE")) {
               return pwdStr;
          }
          else if (pwdEncrypt.equals("RSA")) {
               byte[] dataEnt = RSAUtils.encryptData(pwdStr.getBytes(), TRANSMIT_RSAKEY);
               return Base64.encode(dataEnt);
          }
          else if (pwdEncrypt.equals("RSAMD5")) {
               byte[] dataEnt = RSAUtils.encryptData(EncryptUtils.encodingMD5(pwdStr).getBytes(), TRANSMIT_RSAKEY);
               return Base64.encode(dataEnt);
          }
          else if (pwdEncrypt.equals("3DES")) {
               return DesUtil.encryptString(pwdStr, TRANSMIT_DESKEY);
          }
          else if (pwdEncrypt.equals("3DESMD5")) {
               return DesUtil.encryptString(EncryptUtils.encodingMD5(pwdStr), TRANSMIT_DESKEY);
          }
          else if (pwdEncrypt.equals("MD5")) {
               return EncryptUtils.encodingMD5(pwdStr);
          }
          else {
               return null;
          }
     }
}
