package com.zjsj.mchtapp.config.application;

import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.application.core.BaseApplicationTask;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.DbStore;
import com.ruomm.base.ioc.iocutil.DbStoreSafe;
import com.ruomm.base.tools.TimeUtils;
import com.ruomm.base.tools.androidkeystore.AndroidKeyStoreHelper;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.AppStoreSafeImpl;
import com.zjsj.mchtapp.dal.response.UserInfoDto;

import java.security.KeyPair;

public class AppAplictionTask implements BaseApplicationTask {
    @Override
    public void doTaskOnCreate() {
        AndroidKeyStoreHelper androidKeyStoreHelper=new AndroidKeyStoreHelper();
        KeyPair keyPair=androidKeyStoreHelper.getTargetKeyPair("store_keypair");
        ApiConfig.STORE_KEYPAIR=keyPair;
        AppStoreUtil.setAppStoreSafeInterface(new AppStoreSafeImpl());
        DbStoreSafe.getInstance().setAppStoreSafeInterface(new AppStoreSafeImpl());
        LoginUserFactory.doLoginByStore();
    }


}
