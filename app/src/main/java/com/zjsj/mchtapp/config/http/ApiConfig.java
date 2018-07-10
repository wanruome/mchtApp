package com.zjsj.mchtapp.config.http;

import android.content.Context;
import android.text.TextUtils;

import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.iocutil.BaseServiceUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.DesUtil;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.zjsj.mchtapp.config.LoginUserConfig;
import com.zjsj.mchtapp.module.keypair.KeyPairService;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class ApiConfig {
     public static final String BASE_URL = "http://192.168.100.66:9080/mchtAppUserApi/";
     public static final String TRANSMIT_KEYTYPE="RSA";
     public static String TRANSMIT_DESKEY=null;
     public static PublicKey TRANSMIT_RSAKEY=null;
     public static KeyPair STORE_KEYPAIR=null;
     private static  String uuid=null;
     public static String getAppUUID()
     {
          if(TextUtils.isEmpty(uuid))
          {
               uuid= TelePhoneUtil.getUtdid(BaseApplication.getApplication());
          }
          return uuid;
     }
     public static boolean isKeyPairOk()
     {
//          if(StringUtils.isEmpty(STORE_PUBLICKEY)||StringUtils.isEmpty(STORE_PRIVATEKEY))
//          {
//               return false;
//          }
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
     public static String getPassWordEncrypt(boolean isWithMD5)
     {
          if(!isWithMD5){
               return TRANSMIT_KEYTYPE;
          }
          else{
               return TRANSMIT_KEYTYPE+"MD5";
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
     public static String encryptByApp(String data)
     {
          if(StringUtils.isEmpty(data))
          {
               return data;
          }
          byte[] dataEnt = RSAUtils.encryptDataBig(data.getBytes(), ApiConfig.STORE_KEYPAIR.getPublic());
          return Base64.encode(dataEnt);
     }

     public static String decryptByApp(String data){
          if(StringUtils.isEmpty(data))
          {
               return data;
          }
          byte[] dataEnt = RSAUtils.decryptDataBig(Base64.decode(data),ApiConfig.STORE_KEYPAIR.getPrivate(),1024);
          return new String(dataEnt);
     }
     public static String getPublicKeyString(){
          if("RSA".equals(TRANSMIT_KEYTYPE))
          {
               return  Base64.encode(TRANSMIT_RSAKEY.getEncoded());
          }
          else if("3DES".equals(TRANSMIT_KEYTYPE))
          {
              return TRANSMIT_DESKEY;
          }
          else{
               return "";
          }

     }
     public static Map<String, String> createRequestMap(boolean isLogin) {
          Map<String, String> requestMap = new HashMap<>();
          if (isLogin) {
               requestMap.put("appId", LoginUserConfig.getLoginUserInfo().appId);
               requestMap.put("userId", LoginUserConfig.getLoginUserInfo().userId);
               requestMap.put("tokenId", LoginUserConfig.getLoginUserInfo().tokenId);
               requestMap.put("uuid", getAppUUID());
               requestMap.put("timeStamp", System.currentTimeMillis() + "");
          }
          else {
               requestMap.put("appId","1000");
               requestMap.put("uuid", getAppUUID());
               requestMap.put("uuidEncrypt", ApiConfig.getPassWordEncrypt(false));
               requestMap.put("timeStamp", System.currentTimeMillis() + "");
          }
          return requestMap;
     }
     public static void  signRequestMap(Map<String, String> maps){
          String value = SignTools.getKeyString(maps);
          if(maps.containsKey("userId"))
          {
               value= value+ "token=" + LoginUserConfig.getLoginUserInfo().token;
          }
          else {
               value= value+ "token=" + ApiConfig.getPublicKeyString();
          }
          String signInfo=EncryptUtils.encodingMD5(value);
          maps.put("signInfo",signInfo);
     }

}
