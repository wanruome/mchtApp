package com.zjsj.mchtapp.config.http;

import com.ruomm.base.ioc.iocutil.AppStoreSafeInterface;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.DesUtil;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AppStoreSafeImpl implements AppStoreSafeInterface {
//    private static KeyPair keyPair=null;
//
//    public static void configKeyPair(KeyPair mKeyPair)
//    {
////        if(StringUtils.isEmpty(ApiConfig.STORE_PRIVATEKEY)||StringUtils.isEmpty(ApiConfig.STORE_PUBLICKEY)){
////            return;
////        }
////        if(ApiConfig.STORE_KEYTYPE.equals("RSA")){
////            PublicKey publicKey=RSAUtils.loadPublicKey(ApiConfig.STORE_PUBLICKEY);
////            PrivateKey privateKey=RSAUtils.loadPrivateKey(ApiConfig.STORE_PRIVATEKEY);
////            keyPair= new KeyPair(publicKey,privateKey);
////        }
////        else{
////           desKeyStr=ApiConfig.STORE_PRIVATEKEY;
////        }
//
//
//
//    }
    @Override
    public String encryptStr(String data) {
        return encryptSafeData(data);
    }

    @Override
    public String decryptStr(String data) {
       return decryptSafeData(data);
    }

   private String encryptSafeData(String value) {
        if(null==ApiConfig.STORE_KEYPAIR) {
            return null;
        }
       byte[] dataEnt = RSAUtils.encryptDataBig(value.getBytes(), ApiConfig.STORE_KEYPAIR.getPublic());
       return Base64.encode(dataEnt);
    }
    private String decryptSafeData(String value) {
        if(null==ApiConfig.STORE_KEYPAIR) {
            return null;
        }
        byte[] dataEnt = RSAUtils.decryptDataBig(Base64.decode(value),ApiConfig.STORE_KEYPAIR.getPrivate(),1024);
        return new String(dataEnt);
    }
}
