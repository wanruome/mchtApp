package com.zjsj.mchtapp.config.impl;

import com.ruomm.base.ioc.iocutil.AppStoreSafeInterface;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.DesUtil;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.config.http.ApiConfig;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AppStoreSafeImpl implements AppStoreSafeInterface {

    @Override
    public String encryptStr(String data) {
        return encryptSafeData(data);
    }

    @Override
    public String decryptStr(String data) {
       return decryptSafeData(data);
    }

   private String encryptSafeData(String value) {
        if(null== ApiConfig.STORE_KEYPAIR) {
            return null;
        }
        return ApiConfig.encryptByApp(value);
    }
    private String decryptSafeData(String value) {
        if(null==ApiConfig.STORE_KEYPAIR) {
            return null;
        }
        return ApiConfig.decryptByApp(value);
    }
}
