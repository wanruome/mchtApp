package com.zjsj.mchtapp.config.application;

import com.ruomm.base.http.config.ResponseText;
import com.ruomm.base.http.config.impl.TextHttpCallBack;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.application.core.BaseApplicationTask;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.tools.androidkeystore.AndroidKeyStoreHelper;
import com.ruomm.baseconfig.debug.MLog;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.http.AppStoreSafeImpl;
import com.zjsj.mchtapp.dal.response.KeyPairDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public class AppAplictionTask implements BaseApplicationTask {
    @Override
    public void doTaskOnCreate() {
        AndroidKeyStoreHelper androidKeyStoreHelper=new AndroidKeyStoreHelper();
        KeyPair keyPair=androidKeyStoreHelper.getTargetKeyPair("store_keypair");
        ApiConfig.STORE_KEYPAIR=keyPair;
        AppStoreUtil.setAppStoreSafeInterface(new AppStoreSafeImpl());
    }
    private void getPublicKeyByUuid(){
        String uuid= TelePhoneUtil.getUtdid(BaseApplication.getApplication());
        KeyPair keyPair= RSAUtils.generateRSAKeyPair();
        Map<String,String> map=new HashMap<>();
        map.put("uuid", uuid);
        map.put("keyType", ApiConfig.TRANSMIT_KEYTYPE);
        map.put("rasPublicKey", Base64.encode(keyPair.getPublic().getEncoded()));
        map.put("timeStamp", System.currentTimeMillis() + "");
        ResponseText responseText=
                new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/keypair/getPublicKeyByUuid").setRequestBodyText(map).doHttpSync(KeyPairDto.class, new TextHttpCallBack() {
                    @Override
                    public void httpCallBack(Object resultObject, String resultString, int status) {
                        MLog.i(resultString);
                    }
                });
        if(null== ResultFactory.getErrorTip(responseText)){
            KeyPairDto keyPairDto=ResultFactory.getResult(responseText);
            String pubKeyStr=Base64.encode(RSAUtils.decryptDataBig(Base64.decode(keyPairDto.publicKey),keyPair.getPrivate()));
            ApiConfig.loadTransmitKey(pubKeyStr);
        }
    }
}
