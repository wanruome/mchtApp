package com.zjsj.mchtapp.config.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruomm.base.http.config.TextHttpListener;
import com.ruomm.base.ioc.activity.AppManager;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.dal.event.TokenEvent;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.module.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

public abstract class TextHttpCallBack implements TextHttpListener{


    @Override
    public boolean httpCallBackFilter(String resultString, int status) {
        String code=null;
        try{
            JSONObject jsonObject=null;
            if(!StringUtils.isEmpty(resultString)) {
                jsonObject = JSON.parseObject(resultString);
            }
            if(null!=jsonObject)
            {
                code=jsonObject.getString("code");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            code=null;
        }
        if(null!=code&& ResultFactory.ERR_TOKEN_INVALID.equals(code)) {
            LoginUserFactory.doLogout();
            AppManager.finishAllActivityExceptOne(MainActivity.class);
            TokenEvent tokenInValidEvent=new TokenEvent();
            tokenInValidEvent.isInValid=true;
            EventBus.getDefault().post(tokenInValidEvent);

            return true;
        }
        return false;

    }
}
