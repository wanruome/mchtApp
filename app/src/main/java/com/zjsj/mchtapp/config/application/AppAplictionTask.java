package com.zjsj.mchtapp.config.application;

import android.os.Handler;
import android.os.SystemClock;

import com.ruomm.base.http.config.ResponseText;
import com.ruomm.base.http.config.impl.TextHttpCallBack;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.application.core.BaseApplicationTask;
import com.ruomm.base.ioc.extend.Thread_CanStop;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.BaseServiceUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.tools.androidkeystore.AndroidKeyStoreHelper;
import com.ruomm.baseconfig.debug.MLog;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.http.AppStoreSafeImpl;
import com.zjsj.mchtapp.dal.response.KeyPairDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.module.keypair.KeyPairService;

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


}
