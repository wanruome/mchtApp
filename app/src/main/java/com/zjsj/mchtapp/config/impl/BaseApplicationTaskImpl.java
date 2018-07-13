package com.zjsj.mchtapp.config.impl;

import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.task.BaseApplicationTask;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.DbStoreSafe;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.tools.androidkeystore.AndroidKeyStoreHelper;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.dal.response.KeyPairDto;

import java.security.KeyPair;

public class BaseApplicationTaskImpl implements BaseApplicationTask {
    @Override
    public void doTaskOnCreate() {
        AndroidKeyStoreHelper androidKeyStoreHelper=new AndroidKeyStoreHelper();
        KeyPair keyPair=androidKeyStoreHelper.getTargetKeyPair("store_keypair");
        ApiConfig.STORE_KEYPAIR=keyPair;
        AppStoreUtil.setAppStoreSafeInterface(new AppStoreSafeImpl());
        DbStoreSafe.getInstance().setAppStoreSafeInterface(new AppStoreSafeImpl());
        LoginUserFactory.doLoginByStore();
        KeyPairDto keyPairDto= AppStoreUtil.safeGetBean(BaseApplication.getApplication(),null,KeyPairDto.class);
        if(null!=keyPairDto&&ApiConfig.TRANSMIT_KEYTYPE.equals(keyPairDto.keyType)&& TelePhoneUtil.getUtdid(BaseApplication.getApplication()).equals(keyPairDto.uuid))
        {
            ApiConfig.loadTransmitKey(keyPairDto);
        }

    }


}
