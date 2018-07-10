package com.zjsj.mchtapp.config.keyboard;

import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.util.keyboard.KeyboardSafeInterface;

public class KeyboardSafeImpl implements KeyboardSafeInterface {
    @Override
    public boolean isEncryptByChar() {
        return false;
    }

    @Override
    public String getShowStr(String str) {
        return "*";
    }

    @Override
    public String getEncryptStr(String str) {
        if(StringUtils.isEmpty(str))
        {
            return null;
        }
        else{
            byte[] dataEnt = RSAUtils.encryptDataBig(str.getBytes(), ApiConfig.STORE_KEYPAIR.getPublic());
            return Base64.encode(dataEnt);
        }

    }

    @Override
    public String getDecryptStr(String encryptStr) {
        if(StringUtils.isEmpty(encryptStr))
        {
            return null;
        }
        byte[] dataEnt = RSAUtils.decryptDataBig(Base64.decode(encryptStr), ApiConfig.STORE_KEYPAIR.getPrivate(),1024);
        return new String(dataEnt);
    }
}
