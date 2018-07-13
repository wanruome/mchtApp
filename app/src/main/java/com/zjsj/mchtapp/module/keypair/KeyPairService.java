package com.zjsj.mchtapp.module.keypair;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.ruomm.base.http.config.ResponseText;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.extend.BaseService;
import com.ruomm.base.ioc.extend.Thread_CanStop;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.baseconfig.debug.MLog;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.response.KeyPairDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public class KeyPairService extends BaseService {
    public static final int OptStart=1;
    public boolean isOnRequest=false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        int value= getOptValue(intent);
        if(value==OptStart&&!isOnRequest&&!ApiConfig.isKeyPairOk())
        {
            new DownLoadThread().start();
        }
        else if(ApiConfig.isKeyPairOk()){
            stopSelf();
        }
    }

    private String uuid=ApiConfig.getAppUUID();
    class DownLoadThread extends Thread_CanStop{
        @Override
        public void run() {
            super.run();
            isOnRequest=true;
            while (ApiConfig.isKeyPairOk()){
                getPublicKeyByUuid();
                //失败了的话30秒后重新发起
                SystemClock.sleep(1000l*60);
            }
//            final KeyPairEvent keyPairEvent=new KeyPairEvent();
//            keyPairEvent.isKeyPairOk=ApiConfig.isKeyPairOk();
//            EventBus.getDefault().post(keyPairEvent);
            Handler mHandler= new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    KeyPairService.this.stopSelf();
                }
            });
            mHandler=null;
            isOnRequest=false;
        }
    }
    private void getPublicKeyByUuid(){
        final KeyPair keyPair= RSAUtils.generateRSAKeyPair();
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
        if(null==ResultFactory.getErrorTip(responseText)){
            KeyPairDto keyPairDto=ResultFactory.getResult(responseText);
            String pubKeyStr=new String(RSAUtils.decryptDataBig(Base64.decode(keyPairDto.publicKey),keyPair.getPrivate()));
            ApiConfig.loadTransmitKey(keyPairDto);
        }
    }

}
